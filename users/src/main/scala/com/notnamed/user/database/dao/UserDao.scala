package com.notnamed.user.database.dao
import java.util.UUID

import com.notnamed.commons.database.BaseDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.database.table.Users
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


class UserDao(val db: Database)(implicit val ec: ExecutionContext) extends BaseDao[Users,User] {
  override def table = TableQuery[Users]

  def insert(user: User) : Future[UUID] = db.run {
    table += user
  }.map(insertionCheck(user))
    .map(_ => user.id)

  def findById(id: UUID) : Future[Option[User]] = db.run {
    table
      .filter( x=> x.id === id)
      .result
  }.map(_.headOption)
}
