package com.notnamed.user

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.notnamed.commons.time.TimeProvider
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.routes.UserRoutes
import com.notnamed.user.service.UserService
import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile.api._

class Main {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    Http().bindAndHandle(
      routes,
      config.getString("http.interface"),
      config.getInt("http.port")
    )

  }

  def routes : Route = {
    val db = Database.forConfig("db")
    implicit val timeProvider : TimeProvider = TimeProvider
    val userDao = new UserDao(db)
    val userService = new UserService(userDao)
    val userRoutes = new UserRoutes(userService)

    userRoutes.routes
  }
}
