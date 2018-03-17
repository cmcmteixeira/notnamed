package com.notnamed.user

import java.time.Clock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.user.database.dao.UserDao
import com.notnamed.user.routes.UserRoutes
import com.notnamed.user.service.UserService
import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile

object Users {
  import slick.jdbc.MySQLProfile.api._

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    Http().bindAndHandle(
      routes,
      Config.http.interface,
      Config.http.port
    )
  }

  def routes : Route = {
    val db: MySQLProfile.backend.Database = Database.forConfig("database")
    implicit val timeProvider : TimeProvider = new TimeProvider(Clock.systemUTC())

    val userDao = new UserDao(db)
    val userService = new UserService(userDao, new UUIDGenerator)
    val userRoutes = new UserRoutes(userService)

    //val producer: ProducerSettings[Array[Byte], String] = ProducerSettings(Config.config, new ByteArraySerializer, new StringSerializer)

    userRoutes.routes()
  }
}
