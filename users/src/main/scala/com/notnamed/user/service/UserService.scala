package com.notnamed.user.service

import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.logging.{FutureLogging, ContextualLogger, UniqueLoggingContext}
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.service.UserService.{NewUser, UserModel}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

object UserService {
  case class UserModel(id: UUID, email: String)
  case class NewUser(email: String)
}

class UserService(userDao: UserDao,uuidGen: UUIDGenerator)(implicit ec: ExecutionContext, timeProvider: TimeProvider) extends ContextualLogger with FutureLogging {
  def createUser(newUser: NewUser)(implicit requestContext: UniqueLoggingContext) : Future[UUID] = userDao
    .insert(User(
      uuidGen(),
      newUser.email,
      AuditInfo(
        createdOn = timeProvider.now(),
        updatedOn = timeProvider.now()
      )
    ))
    .andThen(logFutureFailure)

  def findUser(id: UUID)(implicit requestContext: UniqueLoggingContext) : Future[Option[UserModel]]= userDao
    .findById(id)
    .andThen{
      case Success(Some(_)) => logger.info(s"Found user with [id:$id]")
      case Success(None) => logger.warn(s"User for [id:$id] was not found")
    }
    .map{ user =>
      user.map(mapUserEntityToUserModel)
    }.andThen(logFutureFailure)

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
