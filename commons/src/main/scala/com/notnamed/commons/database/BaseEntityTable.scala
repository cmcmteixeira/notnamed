package com.notnamed.commons.database

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.entity.{AuditUUIDEntity, AuditInfo}
import slick.relational.RelationalTableComponent

trait BaseEntityTable[T <: AuditUUIDEntity]  { self : RelationalTableComponent#Table[T] =>
  import slick.jdbc.MySQLProfile.api._

  def id = column[UUID]("id",O.PrimaryKey)
  def createdOn = column[Timestamp]("createdOn")
  def updatedOn = column[Timestamp]("updatedOn")
  def deletedOn = column[Option[Timestamp]]("deletedOn")

  def audit = (createdOn,updatedOn,deletedOn) <> (AuditInfo.tupled,AuditInfo.unapply)
}
