package com.notnamed.groups

import java.time.Clock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import slick.jdbc.MySQLProfile.api._

class Users {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val logger = Logging(system, getClass)

 /* def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    Http().bindAndHandle(
      routes,
      Config.http.interface,
      Config.http.port
    )
  }

  def routes : Route = {
    val db = Database.forConfig("database")
    implicit val clock : Clock = Clock.systemUTC()


    val producer: ProducerSettings[Array[Byte], String] = ProducerSettings(Config.config, new ByteArraySerializer, new StringSerializer)
    userRoutes.routes
  }*/
}
