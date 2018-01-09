package com.notnamed.groups

import java.time.Clock

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.notnamed.commons.database.UUIDGenerator
import com.notnamed.commons.directives.CustomDirectives
import com.notnamed.commons.logging.LoggingContext
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.dao.GroupDao
import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class Groups {

  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val logger: LoggingAdapter = Logging(system, getClass)


  def main(args: Array[String]): Unit = {
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
    implicit val timeProvider: TimeProvider = new TimeProvider(clock)
    implicit val ec: ExecutionContext = ExecutionContext.global


    val groupDao = new GroupDao(db,UUIDGenerator.genUUID _)

    CustomDirectives.withRequestContext { context : LoggingContext =>
      ???
    }

    //val groupService = new GroupService(groupDao)


    //val producer: ProducerSettings[Array[Byte], String] = ProducerSettings(Config.config, new ByteArraySerializer, new StringSerializer)

  }

}
