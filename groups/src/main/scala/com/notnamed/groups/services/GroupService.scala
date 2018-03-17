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
import com.notnamed.groups.database.dao.{GroupDao, MembershipDao}
import com.notnamed.groups.database.entity.{Group, Member}
import com.typesafe.scalalogging.StrictLogging
import io.circe.Encoder

import scala.concurrent.{ExecutionContext, Future}

object GroupService extends DefaultJsonFormats{
  import io.circe.generic.semiauto._

  implicit val eventIdEncoder : Encoder[EventId] = deriveEncoder[EventId]
  implicit val groupEventEncoder: Encoder[NewGroupEvent] = deriveEncoder[NewGroupEvent]

  case class NewGroup(name: String, creator: UUID)
  case class Members(memberSince: Timestamp,userId: UUID)
  case class GroupWithMembers(id: UUID, members: List[Members], active: Boolean)

  case class NewGroupEvent(identifier: EventId = Config.events.groupCreation, groupId: UUID) extends KafkaEvent

  case class GroupServiceException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)
}

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

  private def newGroupCreationEvent(groupId: UUID) = NewGroupEvent(Config.events.groupCreation,groupId)

}
