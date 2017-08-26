package com.notnamed.api

import akka.http.javadsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.notnamed.api.database.entity.User
import com.notnamed.api.database.table.Users
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import spray.json._


object Routes extends ApiProtocol {
/*  def apply(db: Database)(implicit ec: ExecutionContext) = logRequestResult("akka-http-request") {
    implicit val userFormat = jsonFormat3(User)
    /*
        val tq = TableQuery.apply[Users]
        val query = tq.filter(_.id === id).result
        val future = db.run(query)
        Stat
    */
    //val tq = TableQuery.apply[Users]
 /*   pathPrefix("api") {
     // GenericCRUDRoutes.genericCrud[User](TableQuery.apply[Users], db)
      /*~
        path(IntNumber) { id =>
          patch {
            entity(as[String]) { jsonStr =>
              val json = jsonStr.parseJson.asJsObject.fields
              val future = db.run(
                tq.filter(_.id === id).result
              ).map({ results =>
                results
                  .headOption
                  .map { originalEntity =>
                    tq.filter(entity => {
                      entity.id === originalEntity.id &&
                      entity.email === originalEntity.email &&
                      entity.password === originalEntity.password
                    }).map(e => {
                      (e.email, e.password)
                    }).update((
                      Try(json.get("email").map(_.convertTo[String]).getOrElse(originalEntity.email)).get,
                      Try(json.get("password").map(_.convertTo[String]).getOrElse(originalEntity.password)).get
                      ))
                  }
              }).flatMap {
                case Some(action) => db.run(action).map(x => Some(x))
                case _ => Future.successful(None)
              }
              onComplete(future) {
                case Success(Some(value)) if value == 1 => complete(StatusCodes.OK,"Ok")
                case Success(Some(value)) => complete(StatusCodes.NotModified,"Not modified")
                case Success(None) => complete(StatusCodes.NotFound,"Not found")
                case _ => complete(StatusCodes.InternalServerError,"")
              }
            }
          }
        }}*/
  }*/
}*/}



