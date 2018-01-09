package com.notnamed.groups.database.dao

import java.util.UUID

import com.notnamed.commons.database.BaseDao
import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.entity.Member
import com.notnamed.groups.database.table.Members
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object MembershipDao{
  case class MembershipFilters(

                              )
}

class MembershipDao(val db: Database)
                   (implicit uuidGen : () => UUID, val ec: ExecutionContext, timeProvider: TimeProvider) extends BaseDao[Members,Member] {

  val table = TableQuery[Members]

  def addMember(groupId: UUID, member: UUID): Future[UUID] = {
    val entity = Member(uuidGen(), member, groupId, AuditInfo(
      timeProvider.now(),
      timeProvider.now()
    ))
    db.run {
      table += entity
    }.map(insertionCheck(entity))
      .map(_ => entity.id)
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


  def findGroupMembers(groupId: UUID): Future[Seq[Member]] = db
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