package com.notnamed.user.service

import com.notnamed.commons.time.TimeProvider
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.database.entity.User
import com.notnamed.user.service.UserService.UserModel
import org.scalatest.{AsyncWordSpec, Matchers}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.{ExecutionContext, Future}

class UserServiceTest extends AsyncWordSpec with MockitoSugar with Matchers with ScalaFutures {
  val now = 0L
  implicit val timeProviderMock = mock[TimeProvider]
  val userDaoMock = mock[UserDao]
  val userService = new UserService(userDaoMock)(ExecutionContext.global,timeProviderMock)

  "createUser(UserModel)" should {
    val newUser = UserModel(
      None,
      "some@email.com"
    )
    val newUserId = 7L
    "create an user by calling the dao and return the created user id" in {
      when(timeProviderMock.now()).thenReturn(now)
      when(userDaoMock.insert(User(None,newUser.email,now,now))).thenReturn(Future.successful(newUserId))
      val future = userService.createUser(newUser)
      whenReady(future) { result =>
         result shouldBe newUserId
      }
    }
    "propagate any thrown errors" in {
      when(timeProviderMock.now()).thenReturn(now)
      when(userDaoMock.insert(User(None,newUser.email,now,now))).thenReturn(Future.failed(new IllegalArgumentException("Some wrong")))
      val future = userService.createUser(newUser)
      recoverToSucceededIf[IllegalArgumentException](future)
    }
  }
}
