package com.notnamed.api

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.notnamed.api.database.entity.User
import com.notnamed.api.database.table.Users
import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile.api._
import com.notnamed.commons.macros.GenericCRUDRoutes._

object Api {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {

    //genericCrud[Users](new Users())
    val config = ConfigFactory.load()
    val db = Database.forConfig("db")
    Http().bindAndHandle(
      Routes.apply(db),
      config.getString("http.interface"), config.getInt("http.port")
    )
  }
}