package com.notnamed.user.routes

import akka.http.scaladsl.server.Directives._
import com.notnamed.commons.directives.CustomDirectives._
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.user.service.UserService
import com.notnamed.user.service.UserService.{NewUser, UserModel}

trait UserRoutesProtocol extends BaseProtocol {
  implicit val jsonFormatUserModel = jsonFormat2(UserModel)
  implicit val jsonFormatNewUserModel = jsonFormat1(NewUser)
}

class UserRoutes(userService :UserService) extends UserRoutesProtocol {
  def routes = logRequestResult("akka-http-request") {
    pathPrefix("user") {
        fetchEntity(userService.findUser) ~
        createEntity(userService.createUser)
    }
  }
}
