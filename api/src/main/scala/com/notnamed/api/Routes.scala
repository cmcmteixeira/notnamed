package com.notnamed.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import com.notnamed.api.database.entity.User
import com.notnamed.api.database.table.Users
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api._
import Protocol._
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.server.Route
import com.notnamed.commons.database.entity.KeyedEntity
import com.notnamed.commons.database.table.KeyedTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Success,Failure}

import com.notnamed.commons.macros.GenericCRUDRoutes._



object Routes extends ApiProtocol {
  /*
    new {
      def a(implicit e: Table[SlickException]) {

      }
    }.a
  */


  /*
  def genericCRUDRoutes[E <: KeyedEntity, T <: Table[E] with KeyedTable](entityName: String, mapper: (E, Int) => E, tq: TableQuery[T], db: Database)
    (implicit fromRequestUnmarshaller: FromRequestUnmarshaller[E], toResponse: ToResponseMarshaller[E], ec: ExecutionContext) = {
      path(entityName) {
        post {entity(as[E]) { e: E =>
          val query = (tq returning tq.map(_.id) into ((entity, id) => entity))
          val future = db.run(query += e)
          onComplete(future){
            case Success(v) => complete(v)
            case Failure(e) => complete("error")
          }
        } ~
        put {entity(as[E]) { e: E => complete {
          "" //complete{db.run(tq.insertOrUpdate(e).result)}
        }}} ~
        path(IntNumber){ id =>
          delete {
            complete(db.run(tq.filter(_.id === id).delete).map(x => "Funfou"))
          } ~
          get{
            complete(db.run(tq.filter(_.id === id).result).map(x => "Funfou"))
          }
        }
      }
    }}*/


  def apply(db: Database)(implicit ec: ExecutionContext) = logRequestResult("akka-http-request") {
    implicit val database = db
    val theRoute =  new final class $anon {
      import akka.http.scaladsl.model.StatusCodes;
      import akka.http.scaladsl.server.Route;
      val db = database
      val tq = TableQuery[Users]
      def apply(implicit unmarshaller: FromRequestUnmarshaller[com.notnamed.api.database.entity.User], marshaller: ToResponseMarshaller[com.notnamed.api.database.entity.User], ec: ExecutionContext): Route =
        path("com.notnamed.api.database.entity.user")(
          post(entity(as[com.notnamed.api.database.entity.User]))(((e: com.notnamed.api.database.entity.User) => complete{

          }
          )))
    }.apply(database)
    pathPrefix("api") {

    }

    //      genericCRUDRoutes[User, Users]("user", x => x._1.copy(id=Some(x._2)), , db)
  }
}


