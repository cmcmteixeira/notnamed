package com.notnamed.user.database.dao
import com.notnamed.commons.database.KeyedDao
import com.notnamed.commons.time.TimeProvider
import com.notnamed.user.database.entity.User
import com.notnamed.user.database.table.Users
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext


class UserDao(override val db: Database)(implicit val ec: ExecutionContext) extends KeyedDao[Users,User] {
  override def table = TableQuery[Users]
}
