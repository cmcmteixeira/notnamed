package com.notnamed.groups.services

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.logging.{LoggerWithContext, LoggingContext}
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.dao.{GroupDao, MembershipDao}
import com.notnamed.groups.database.entity.{Group, Member}
import com.notnamed.groups.services.GroupService.{GroupWithMembers, Members, NewGroup}

import scala.concurrent.{ExecutionContext, Future}

object GroupService {
  case class NewGroup(name: String, creator: UUID)
  case class Members(memberSince: Timestamp,userId: UUID)
  case class GroupWithMembers(id: UUID, members: List[Members], active: Boolean)
}

class GroupService(groupDao: GroupDao, membershipDao: MembershipDao,uuidGen : () => UUID)(implicit ec: ExecutionContext, timeProvider: TimeProvider)  {

  def createGroup(newGroup: NewGroup): Future[UUID] = groupDao
    .createGroup(Group(
      id = uuidGen(),
      name = newGroup.name,
      createdBy = newGroup.creator,
      audit = AuditInfo(
        timeProvider.now(),
        timeProvider.now()
      )
    ))

  def fetchGroup(groupId: UUID)(implicit lc: LoggingContext) : Future[Option[GroupWithMembers]] = groupDao
    .findGroupById(groupId)
    .flatMap {
      case Some(g) => membershipDao
        .findGroupMembers(g.id)
        .map(m =>  Some((g,m.toList)))
      case None => Future
        .successful(None)
    }.map{ gm =>
      gm.map(Function.tupled(toGroupWMembership))
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

}
