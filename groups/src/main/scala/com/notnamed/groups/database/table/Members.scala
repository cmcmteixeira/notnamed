package com.notnamed.groups.database.table

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.database.BaseEntityTable
import com.notnamed.groups.database.entity.Member
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext


class Members(tag: Tag)(implicit ec: ExecutionContext) extends Table[Member](tag,"user") with BaseEntityTable[Member] {

  def userId = column[UUID]("userId")
  def groupId = column[UUID]("groupId")

  def * = (id, userId, groupId, audit) <> (Member.tupled, Member.unapply)
}
