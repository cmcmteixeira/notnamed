package com.notnamed.user.service

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import com.notnamed.commons.entity.AuditInfo
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.service.UserService.{NewUser, UserModel}
import org.scalatest.{AsyncWordSpec, Matchers}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class UserServiceSpec extends AsyncWordSpec with MockitoSugar with Matchers  {
 /* val now = Timestamp.valueOf(LocalDateTime.now())

  implicit val uuidGenerator = mock[UUIDGenerator]
  implicit val timeProviderMock= mock[TimeProvider]
  val email =       "some@email.com"
  val userDaoMock = mock[UserDao]
  val userId = UUID.fromString("123e4567-e89b-12d3-a456-426655440000")
  when(uuidGenerator.apply()).thenReturn(userId)
  val userService = new UserService(userDaoMock,uuidGenerator)(ExecutionContext.global,timeProviderMock)

  "createUser(NewUser)" should {
    val newUser = NewUser(email)
    val newUserId = uuidGenerator()
    "create an user by calling the dao and return the created user id" in {
      when(userDaoMock.insert(User(newUserId,email,AuditInfo(now,now)))).thenReturn(Future.successful(newUserId))
      when(timeProviderMock.now()).thenReturn(now)
      userService
        .createUser(newUser)
        .map{ result =>
          result shouldBe newUserId
        }
    }
  }

  "findUser(userId)" should {
    val userId = uuidGenerator()
    val user = User(
      userId,
      "some@email.com",
      AuditInfo(
        now,
        now
      )
    )
    "return the user model object" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(Some(user)))
      userService
        .findUser(userId)
        .map { result =>
          result shouldBe Some(UserModel(
            user.id,
            user.email
          ))
        }
    }


    "return None if no matches are found" in {
      when(userDaoMock.findById(userId)).thenReturn(Future.successful(None))
      userService
        .findUser(userId)
        .map{ result =>
        result shouldBe None
      }
    }
  }*/
}
