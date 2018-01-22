package com.notnamed.groups.database.table

import java.util.UUID

import com.notnamed.commons.database.BaseEntityTable
import com.notnamed.groups.database.entity.Group
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext

class Groups(tag: Tag)(implicit ec: ExecutionContext) extends Table[Group](tag,"groups") with BaseEntityTable[Group] {

  def name = column[String]("name",O.Length(128))
  def createdBy = column[UUID]("createdBy")

  def * = (id,name,createdBy,audit) <> (Group.tupled, Group.unapply)
}
