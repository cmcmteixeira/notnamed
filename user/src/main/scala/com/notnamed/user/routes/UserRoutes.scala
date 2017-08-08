package com.notnamed.user.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import com.notnamed.user.service.UserService
import com.notnamed.commons.directives.CustomDirectives._
import com.notnamed.user.service.UserService.UserModel
import spray.json._

trait UserRoutesProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val jsonFormatUserModel = jsonFormat2(UserModel)
}

class UserRoutes(userService :UserService) extends UserRoutesProtocol {

  def routes = logRequestResult("akka-http-request") {
    pathPrefix("user") {
        fetchEntity(userService.findUser _) ~
        createEntity(userService.createUser _)
    }
  }
}
