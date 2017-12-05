package com.notnamed.groups.database.dao

import java.util.UUID

import com.notnamed.commons.database.KeyedDao
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.entity.Member
import com.notnamed.groups.database.table.Members

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

class MembershipDao(val db: Database, val table : TableQuery[Members])
                   (implicit uuidGen : () => UUID, val ec: ExecutionContext, timeProvider: TimeProvider) extends KeyedDao[Members,Member] {

  def addMember(groupId: UUID, member: UUID): Future[UUID] = insert(Member(uuidGen(),member,groupId,None,timeProvider.now(),timeProvider.now()))

  def removeMember(groupId: UUID,member: UUID): Future[Unit] = db.run {
    table
      .filter(_.groupId === groupId)
      .filter(_.userId === member)
      .filter(_.deletedOn.isEmpty)
      .map(_.deletedOn)
      .update(Some(timeProvider.now()))
      .map(singleUpdateCheck)
  }

}