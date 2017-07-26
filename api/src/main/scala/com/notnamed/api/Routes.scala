package com.notnamed.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.notnamed.api.database.entity.User
import com.notnamed.api.database.table.Users
import com.notnamed.commons.macros.GenericCRUDRoutes
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import spray.json._


object Routes extends ApiProtocol {
  def apply(db: Database)(implicit ec: ExecutionContext) = logRequestResult("akka-http-request") {
    implicit val userFormat = jsonFormat3(User)
    /*
        val tq = TableQuery.apply[Users]
        val query = tq.filter(_.id === id).result
        val future = db.run(query)
        Stat
    */
    val tq = TableQuery.apply[Users]
    pathPrefix("api") {
      GenericCRUDRoutes.genericCrud[User](TableQuery.apply[Users],db) ~
        path(IntNumber) { id =>
          patch {
            entity(as[]) { json =>
              val  entity = new User(Some(2),"","")
              val fields = json.obj
              Some(entity)
                .map(a => fields
                  .get("email")
                  .map(_.asInstanceOf[String])
                  .map(x => a.copy(email = x))
                  .getOrElse(a)
                ).map(a => fields
                .get("password")
                .map(_.asInstanceOf[String])
                .map(x => a.copy(email = x))
                .getOrElse(a)
              ) match {
                case Some(newEntity) => tq
                  .filter{ table =>
                    table.id === entity.id && table.email === entity.email && table.password === entity.password
                  }.map(entity => (entity.id,entity.password,entity.email))
                  .update((newEntity.id,newEntity.password,newEntity.email))
                  complete(StatusCodes.OK,newEntity)
                case None => complete(StatusCodes.NotFound,"")
              }

              complete("Done")
            }
          }
        }
    }
  }
}



