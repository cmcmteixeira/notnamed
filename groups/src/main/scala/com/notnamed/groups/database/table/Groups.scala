package com.notnamed.groups.database.table

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.database.EventEntityTable
import com.notnamed.groups.database.entity.Group
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext

class Groups(tag: Tag)(implicit ec: ExecutionContext) extends Table[Group](tag,"group") with EventEntityTable {

  def id = column[UUID]("id")
  def name = column[String]("name",O.Length(512))
  def createdOn = column[Timestamp]("createdOn")
  def owner = column[Timestamp]("createdOn")
  def updatedOn = column[Timestamp]("updatedOn")

  def * = (id,name,createdOn, updatedOn) <> (Group.tupled, Group.unapply)
}
