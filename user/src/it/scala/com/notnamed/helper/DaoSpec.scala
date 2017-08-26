package com.notnamed.helper

import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll}
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.SQLActionBuilder
import slick.jdbc.SetParameter.SetUnit

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

trait DaoSpec extends AsyncWordSpec with BeforeAndAfterAll {
  val db : Database

  override def beforeAll() = {
    implicit val executionContext: ExecutionContext = ExecutionContext.global
    val truncation =  db
      .run(sql"SHOW TABLES;".as[String])
      .flatMap(tables =>
        Future.traverse(tables)(table => db.run(SQLActionBuilder(List(s"DELETE FROM $table WHERE 1=1"), SetUnit).asUpdate))
      ).map(println)
    Await.result(truncation, 10 seconds)
  }
}
