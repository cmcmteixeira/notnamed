package com.notnamed.user.routes

import akka.http.javadsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.notnamed.commons.macros.GenericCRUDRoutes
import com.notnamed.user.service.UserService
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import spray.json._
import com.notnamed.commons.directives.CustomDirectives._



trait UserRoutes{
  def userService :UserService

  def userRoutes = logRequestResult("akka-http-request") {
    pathPrefix("user") {
      entityFetching(userService.findUser _) ~
      entityCreation(userService.createUser _)
    }
  }
}
