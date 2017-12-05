package com.notnamed.user.database.table

import com.notnamed.commons.database.EventEntityTable
import com.notnamed.user.database.entity.User
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext

class Users (tag: Tag)(implicit ec: ExecutionContext) extends Table[User](tag,"user") with EventEntityTable {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.Length(512))
  def createdOn = column[Long]("createdOn", O.Length(512))
  def updatedOn = column[Long]("updatedOn", O.Length(512))

  def * = (id.?, email, createdOn, updatedOn) <> (User.tupled, User.unapply)
}
