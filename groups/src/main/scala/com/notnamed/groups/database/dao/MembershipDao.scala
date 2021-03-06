package com.notnamed.groups.database.dao

import java.util.UUID

import com.notnamed.commons.database.BaseDao
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.entity.Membership
import com.notnamed.groups.database.table.Members
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object MembershipDao{
  case class MembershipFilters(

                              )
}

class MembershipDao(val db: Database)
                   (implicit ec: ExecutionContext, timeProvider: TimeProvider) extends BaseDao[Members,Membership] {

  val table = TableQuery[Members]

  def addMember(member: Membership): Future[UUID] = {
    db.run {
      table += member
    }.map(insertionCheck(member))
    .map(_ => member.id)
  }

  def removeMember(groupId: UUID, member: UUID): Future[Unit] = db.run {
    withMembershipFilters(
      active = Some(false),
      groups = Some(List(groupId)),
      users = Some(List(member))
    )
      .map(_.deletedOn)
      .update(Some(timeProvider.now()))
  }.map(singleUpdateCheck)


  def findGroupMembers(groupId: UUID): Future[Seq[Membership]] = db
    .run {
      withMembershipFilters(
        groups = Some(List(groupId))
      ).result
    }

  def withMembershipFilters(
                             groups: Option[Traversable[UUID]] = None,
                             users: Option[Traversable[UUID]] = None,
                             active: Option[Boolean] = None) = {
    baseFilterApplier(
      groups.map(groupIds => (members: BaseDaoQuery) => members.filter(_.groupId inSet groupIds)),
      users.map(users => (members: BaseDaoQuery) => members.filter(_.userId inSet users)),
      active.map {
        case false => (users: BaseDaoQuery) => users.filter(_.deletedOn.isEmpty)
        case true => (users: BaseDaoQuery) => users.filter(_.deletedOn.isDefined)
      }
    )
  }
}