package com.notnamed.groups.services

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.kafka.Action.Action
import com.notnamed.commons.kafka.{Action, ActionEvent, KafkaTopicProducer, MetaEventInfo}
import com.notnamed.commons.logging.{FutureLogging, LoggerWithContext, RequestContext, TraceContext}
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.groups.dal.UserDal
import com.notnamed.groups.database.dao.{GroupDao, MembershipDao}
import com.notnamed.groups.database.entity.{Group, Member}

import scala.concurrent.{ExecutionContext, Future}

object GroupService extends BaseProtocol {

  case class NewGroup(name: String, creator: UUID)
  case class Members(memberSince: Timestamp,userId: UUID)
  case class GroupWithMembers(id: UUID, members: List[Members], active: Boolean)
  case class GroupServiceException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)

  case class GroupEvent(id: UUID,createdBy: UUID, name: String)

  implicit val action = new EnumJsonConverter(Action)
  implicit val meta = jsonFormat2(MetaEventInfo)
  implicit val groupEventFormat = jsonFormat3(GroupEvent)
  implicit val groupActionEventFormat = jsonFormat3(ActionEvent[GroupEvent])
}

class GroupService(
                    groupDao: GroupDao,
                    membershipDao: MembershipDao,
                    userDal: UserDal,
                    uuidGen: UUIDGenerator,
                    eventSink: KafkaTopicProducer,
                  )(implicit ec: ExecutionContext, timeProvider: TimeProvider) extends LoggerWithContext with FutureLogging {
  import GroupService._
  def createGroup(newGroup: NewGroup)(implicit requestContext: RequestContext): Future[UUID] = userDal
    .fetchUserInfo(newGroup.creator)
    .flatMap( _ =>
      groupDao.createGroup(newGroupToGroup(newGroup))
    )
    .flatMap( groupId =>
      eventSink
        .send(newGroupCreationEvent(groupId,newGroup))
        .map(_ => groupId)
    )
    .andThen(logFutureFailure)


  def fetchGroup(groupId: UUID)(implicit lc: TraceContext) : Future[Option[GroupWithMembers]] = groupDao
    .findGroupById(groupId)
    .flatMap {
      case Some(g) => membershipDao
        .findGroupMembers(g.id)
        .map(m =>  Some((g,m.toList)))
      case None => Future
        .successful(None)
    }.map{ gm =>
    gm.map(Function.tupled(toGroupWMembership))
  }.andThen {
    case a => logger.debug(a.toString)
  }

  def toGroupWMembership(group: Group, members: List[Member]) = GroupWithMembers(
    group.id,
    members.map(toMember),
    group.audit.deletedOn.isEmpty
  )

  def toMember(member : Member) = Members(
    member.audit.createdOn,
    member.userId
  )


  private def newGroupToGroup(newGroup: NewGroup) = Group(
    id = uuidGen(),
    name = newGroup.name,
    createdBy = newGroup.creator,
    audit = AuditInfo(
      timeProvider.now(),
      timeProvider.now(),
      None
    )
  )

  private def newGroupCreationEvent(groupId: UUID, newGroup: NewGroup) = ActionEvent[GroupEvent](
    Action.Created,
    MetaEventInfo(timeProvider.now(), "groupService"),
    details = GroupEvent(groupId, newGroup.creator, newGroup.name)
  )

}
