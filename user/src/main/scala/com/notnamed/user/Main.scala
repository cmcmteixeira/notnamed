package com.notnamed.user

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import slick.jdbc.MySQLProfile.api._

class Main {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {
    val db = Database.forConfig("db")
  }
}
