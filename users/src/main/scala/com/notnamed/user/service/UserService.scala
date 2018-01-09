package com.notnamed.user.service

import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.time.TimeProvider
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.service.UserService.{NewUser, UserModel}

import scala.concurrent.{ExecutionContext, Future}

object UserService {
  case class UserModel(id: UUID, email: String)
  case class NewUser(email: String)
}

class UserService(userDao: UserDao, uuidGen: () => UUID)(implicit ec: ExecutionContext, timeProvider: TimeProvider) {
  def createUser(newUser: NewUser) : Future[UUID] = userDao
    .insert(User(
      uuidGen(),
      newUser.email,
      AuditInfo(
        createdOn = timeProvider.now(),
        updatedOn = timeProvider.now()
      )
    ))

  def findUser(id: UUID) : Future[Option[UserModel]]= userDao
    .findById(id)
    .map{ user =>
      user.map(mapUserEntityToUserModel)
    }

  private def mapUserModelToUserEntity(userModel: UserModel) = User(
    userModel.id,
    userModel.email,
    AuditInfo(
      timeProvider.now(),
      timeProvider.now()
    )
  )

  private def mapUserEntityToUserModel(user: User) = UserModel(
    user.id,
    user.email
  )
}
