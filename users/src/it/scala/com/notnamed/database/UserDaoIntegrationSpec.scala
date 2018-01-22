package com.notnamed.database

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.helper.DaoSpec
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.service.UserService
import com.notnamed.user.service.UserService.{NewUser, UserModel}
import org.scalatest.Matchers
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.ExecutionContext

class UserDaoIntegrationSpec extends DaoSpec with MockitoSugar with Matchers {
  implicit val timeProviderMock = mock[TimeProvider]
  val uuidGenMock = mock[UUIDGenerator]
  val now = Timestamp.valueOf(LocalDateTime.now)
  val userDao = new UserDao(database)(ExecutionContext.global)
  val userService = new UserService(userDao, uuidGenMock)(ExecutionContext.global, timeProviderMock)

  def userEmail = s"user${System.currentTimeMillis()}@tests.com"

  "The UserDao" should {
    "create users" in {
      val uuid = UUID.randomUUID()
      when(uuidGenMock.apply()).thenReturn(uuid)
      userService
        .createUser(NewUser(userEmail))
        .map(result => {
          result shouldBe uuid
        })
    }

    "allow the retrieval of created users" in {
      val uuid = UUID.randomUUID()
      when(uuidGenMock.apply()).thenReturn(uuid)
      userService
        .createUser(NewUser(userEmail))
        .flatMap { createdUser =>
          userService.findUser(createdUser)
        }.map { result =>
        result shouldBe Some(UserModel(uuid, userEmail))
      }
    }

    "fail if an attempt is made to create two users w/ same email are" in {
      when(uuidGenMock.apply()).thenReturn(UUID.randomUUID())
      when(uuidGenMock.apply()).thenReturn(UUID.randomUUID())
      recoverToSucceededIf[Throwable]{
        userService
          .createUser(NewUser(userEmail))
          .flatMap(_ =>userService.createUser(NewUser(userEmail)))
      }
    }

    "return None for non existent userIds" in {
      userService
        .findUser(UUID.randomUUID())
        .map { result =>
          result shouldBe None
        }
    }
  }
}
