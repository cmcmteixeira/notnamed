package com.notnamed.commons.directives

import java.util.UUID

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._

import scala.concurrent.Future

class CustomDirectivesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  "someOr404" should {

    "return 404 if a future of None is returned" in {
      val route = get {
        CustomDirectives.someOr404[String](Future.successful[Option[String]](None))
      }
      Get() ~> route ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
    "return status code 200 and the value if the value is returned" in {
      val route = get {
        CustomDirectives.someOr404[String](Future.successful(Some("thing")))
      }
      Get() ~> route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "thing"
      }
    }

    "return a 500 if an internal exception is thrown" in {
      val route = get {
        CustomDirectives.someOr404[String](Future.failed(new Exception("lol")))
      }
      Get() ~> route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[String] shouldBe ""
      }
    }
  }

  "createdOr500" should {
    "return created if the future is successful" in {
        val route = post {
          CustomDirectives.createdOr500(Future.successful(UUID.randomUUID()))
        }
      Post() ~> route ~> check {
        status shouldBe StatusCodes.Created
      }
    }

    "return a status code 500 " in {
      val route = post {
        CustomDirectives.createdOr500(Future.failed(new Exception("something")))
      }
      Post() ~> route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }
  }


  "createEntity" should {
    "return created if the future is successful" in {
      val route = post {
        CustomDirectives.createEntity[String]((_) => Future.successful(UUID.randomUUID()))
      }
      Post("/submit",Some("thing")) ~> route ~> check {
        status shouldBe StatusCodes.Created
      }
    }

    "return a status code 500 " in {
      val route = post {
        CustomDirectives.createEntity[String]((_) => Future.failed[UUID](new Exception("something")))
      }
      Post("/submit",Some("thing")) ~> route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }
  }

  "fetchEntity" should {

    "return 404 if a future of None is returned" in {
      val route = get {
        CustomDirectives.fetchEntity[String]((_) => Future.successful(None))
      }
      Get("/067e6162-3b6f-4ae2-a171-2470b63dff00") ~> route ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return status code 200 and the value if the value is found" in {
      val route = get {
        CustomDirectives.fetchEntity[String]((entity) => Future.successful(Some(entity.toString)))
      }
      Get("/067e6162-3b6f-4ae2-a171-2470b63dff00",Some("random")) ~> route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "067e6162-3b6f-4ae2-a171-2470b63dff00"
      }
    }

    "return a 500 if an internal exception is thrown" in {
      val route = get {
        CustomDirectives.fetchEntity[String]((_) => Future.failed[Option[String]](new Exception("lol")))
      }
      Get("/067e6162-3b6f-4ae2-a171-2470b63dff00") ~> route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[String] shouldBe ""
      }
    }
  }


}
