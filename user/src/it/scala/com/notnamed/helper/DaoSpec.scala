package com.notnamed.helper

import com.notnamed.user.Config
import org.scalatest.{AsyncWordSpec, BeforeAndAfterEach, ConfigMap}
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.SQLActionBuilder
import slick.jdbc.SetParameter.SetUnit

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import org.flywaydb.core.Flyway

trait DaoSpec extends AsyncWordSpec with BeforeAndAfterEach  {
  val database = Database.forConfig("database")
  override def beforeEach() : Unit = {
    val flyway = new Flyway()
    flyway.setDataSource(Config.database.url, Config.database.user, Config.database.password)
    flyway.migrate()

    implicit val executionContext: ExecutionContext = ExecutionContext.global
    val truncation =  database
      .run(sql"SHOW TABLES;".as[String])
      .flatMap(tables =>
        Future.traverse(tables.filterNot(_ == "schema_version"))(table => database.run(SQLActionBuilder(List(s"DELETE FROM $table WHERE 1=1"), SetUnit).asUpdate))
    )
    Await.result(truncation, 10 seconds)
  }
}
