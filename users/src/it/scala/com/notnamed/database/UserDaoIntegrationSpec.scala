package com.notnamed.database

import java.util.UUID

import com.notnamed.commons.time.TimeProvider
import com.notnamed.helper.DaoSpec
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.service.UserService
import com.notnamed.user.service.UserService.UserModel
import org.mockito.Mockito._
import org.scalatest.Matchers
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext

class UserDaoIntegrationSpec extends DaoSpec with MockitoSugar with Matchers with ScalaFutures with IntegrationPatience {
/*
  val now = 100L
  implicit val timeProviderMock = mock[TimeProvider]
  val userDao = new UserDao(database)(ExecutionContext.global)
  val userService = new UserService(userDao, UUID.randomUUID _)(ExecutionContext.global,timeProviderMock)
  def userGenerator() = UserModel(None, s"some${System.currentTimeMillis()}@email.com")
  when(timeProviderMock.now()).thenReturn(now)

  "The UserDao" should {
    "create users" in {
      val userCreationFuture = userService.createUser(userGenerator())
      whenReady(userCreationFuture){ result =>
        result > 0 shouldBe true
      }
    }

    "allow the retrieval of created users" in {
      val newUser = userGenerator()
      userService
        .createUser(newUser)
        .flatMap { createdUser =>
          whenReady(userService.findUser(createdUser)) { result =>
            result shouldBe Some(newUser.copy(id = Some(createdUser)))
          }
        }
    }

    "return None for non existent userIds" in {
      whenReady(userService.findUser(Long.MaxValue)) { result =>
        result shouldBe None
      }
    }
  }*/

}
