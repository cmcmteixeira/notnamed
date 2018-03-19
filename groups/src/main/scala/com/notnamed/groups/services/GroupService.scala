package com.notnamed.groups.services

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.formats.DefaultJsonFormats
import com.notnamed.commons.kafka._
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.groups.Config
import com.notnamed.groups.dal.UserDal
import com.notnamed.groups.dal.UserDal.User
import com.notnamed.groups.database.dao.{GroupDao, MembershipDao}
import com.notnamed.groups.database.entity.{Group, Membership}
import com.typesafe.scalalogging.StrictLogging
import io.circe.Encoder

import scala.concurrent.{ExecutionContext, Future}



class GroupService(
                    groupDao: GroupDao,
                    membershipDao: MembershipDao,
                    userDal: UserDal,
                    uuidGen: UUIDGenerator,
                    eventSink: KafkaTopicProducer,
                  )(implicit ec: ExecutionContext, timeProvider: TimeProvider)  extends StrictLogging  {
  import GroupService._

  def createGroup(newGroup: NewGroup): Future[UUID] = userDal
    .fetchUserInfo(newGroup.creator)
    .flatMap( _ =>
      groupDao.createGroup(newGroupToGroup(newGroup))
    )
    .flatMap( groupId =>
      eventSink
        .send(newGroupCreationEvent(groupId))
        .map(_ => groupId)
    )


  def fetchGroup(groupId: UUID): Future[Option[GroupWithMembers]] = groupDao
    .findGroupById(groupId)
    .flatMap {
      case Some(group) => membershipDao
        .findGroupMembers(group.id)
        .map(m =>  Some((group,m.toList)))
      case None => Future
        .successful(None)
    }.map{ group =>
      group.map(Function.tupled(toGroupWMembership))
    }

  def addMember(groupId : UUID)(member: NewGroupMember) : Future[UUID]= groupDao
    .findGroupById(groupId)
    .flatMap { group =>
      group
        .map(Future.successful)
        .getOrElse(Future.failed(GroupServiceException(s"Group $groupId not found. Can't add member to non existent group")))
    }
    .flatMap{ group =>
      userDal
        .fetchUserInfo(member.userId)
        .map((group, _))
    }
    .flatMap{ case (group, userInfo) =>
      membershipDao
        .addMember(newMembership(group,userInfo))
    }


  private def toGroupWMembership(group: Group, members: List[Membership]) = GroupWithMembers(
    group.id,
    members.map(toMember),
    group.audit.deletedOn.isEmpty
  )

  private def toMember(member : Membership) = Members(
    member.audit.createdOn,
    member.userId
  )

  private def newGroupToGroup(newGroup: NewGroup) = Group(
    id = uuidGen(),
    name = newGroup.name,
    createdBy = newGroup.creator,
    audit = auditInfo()
  )

  private def newMembership(group: Group, member: User) = {
    Membership(
      uuidGen(),
      member.id,
      group.id,
      auditInfo()
    )
  }

  private def auditInfo() = AuditInfo(
    timeProvider.now(),
    timeProvider.now(),
    None
  )

  private def newGroupCreationEvent(groupId: UUID) = NewGroupEvent(Config.events.groupCreation,groupId)

}

object GroupService extends DefaultJsonFormats{
  import io.circe.generic.semiauto._

  implicit val eventIdEncoder : Encoder[EventId] = deriveEncoder[EventId]
  implicit val groupEventEncoder: Encoder[NewGroupEvent] = deriveEncoder[NewGroupEvent]

  case class NewGroup(name: String, creator: UUID)
  case class NewGroupMember(userId: UUID)

  case class Members(memberSince: Timestamp,userId: UUID)
  case class GroupWithMembers(id: UUID, members: List[Members], active: Boolean)

  case class NewGroupEvent(eventId: EventId = Config.events.groupCreation, groupId: UUID) extends KafkaEvent

  case class GroupServiceException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)
}