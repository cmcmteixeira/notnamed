package com.notnamed.user.database.table

import com.notnamed.commons.database.BaseEntityTable
import com.notnamed.user.database.entity.User
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.ExecutionContext

class Users(tag: Tag)(implicit ec: ExecutionContext) extends Table[User](tag,"user") with BaseEntityTable[User] {
  def email = column[String]("email", O.Length(512))
  def * = (id, email, audit) <> (User.tupled, User.unapply)
}
