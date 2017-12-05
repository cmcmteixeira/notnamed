package com.notnamed.groups.database.table

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.database.EventEntityTable
import com.notnamed.groups.database.entity.Member
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext


class Members(tag: Tag)(implicit ec: ExecutionContext) extends Table[Member](tag,"user") with EventEntityTable {

  def id = column[UUID]("id")
  def userId = column[UUID]("userId")
  def groupId = column[UUID]("groupId")
  def updatedOn = column[Timestamp]("updatedOn")
  def deletedOn = column[Option[Timestamp]]("deletedOn")
  def createdOn = column[Timestamp]("createdOn")

  def * = (id, userId, groupId, deletedOn, createdOn, updatedOn) <> (Member.tupled, Member.unapply)
}
