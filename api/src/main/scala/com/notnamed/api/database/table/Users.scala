package com.notnamed.api.database.table

import com.notnamed.api.database.entity.User
import slick.lifted.Tag
import slick.jdbc.MySQLProfile.api._


class Users(tag: Tag) extends Table[User](tag,"user")  {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.Length(512))
  def password = column[String]("password", O.Length(512))
  def * = (id.?, email, password) <> (User.tupled, User.unapply)
}