package com.notnamed.user.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.user.service.UserService
import com.notnamed.user.service.UserService.{NewUser, UserModel}

import scala.concurrent.ExecutionContext

trait UserRoutesProtocol extends BaseProtocol {
  implicit val jsonFormatUserModel = jsonFormat2(UserModel)
  implicit val jsonFormatNewUserModel = jsonFormat1(NewUser)
}

class UserRoutes(userService :UserService) extends UserRoutesProtocol {

  def routes()(implicit ec: ExecutionContext) : Route  =
    pathPrefix("user") {
      (path(JavaUUID) & get & rejectEmptyResponse) { uuid =>
          complete(userService.findUser(uuid))
      } ~
      (post & entity(as[NewUser])){ entity =>
        complete(userService.createUser(entity).map(_.toString))
      }
    }
}
