package com.notnamed.user.service

import com.notnamed.commons.time.TimeProvider
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.service.UserService.UserModel
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object UserService {
  case class UserModel(id: Option[Long], email: String)
}

class UserService(userDao: UserDao)(implicit ec: ExecutionContext, timeProvider: TimeProvider) {
  def createUser(user: UserModel) : Future[Long] = userDao
    .insert(mapUserModelToUserEntity(user))

  def findUser(id: Long) : Future[Option[UserModel]]= userDao
    .findById(id)
    .map(_.map(mapUserEntityToUserModel))

  private def mapUserModelToUserEntity(userModel: UserModel) = User(
    userModel.id,
    userModel.email,
    timeProvider.now(),
    timeProvider.now()
  )

  private def mapUserEntityToUserModel(user: User) = UserModel(
    user.id,
    user.email
  )
}
