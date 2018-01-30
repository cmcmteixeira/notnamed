package com.notnamed.commons.directives

import java.util.UUID

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Directive1, Route}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import com.notnamed.commons.logging.UniqueLoggingContext

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object CustomDirectives extends CustomDirectives

trait CustomDirectives {
  def someOr404[T](future: Future[Option[T]])(implicit marshaller: ToEntityMarshaller[T]): Route = {
    onComplete(future){
      case Success(Some(value)) => complete((StatusCodes.OK, value))
      case Success(None) => complete(StatusCodes.NotFound,"Not Found")
      case Failure(_) => complete(StatusCodes.InternalServerError,"")
    }
  }

  def createdOr500(future: Future[UUID]) : Route = {
    onComplete(future){
      case Success(uuid) => complete(StatusCodes.Created, uuid.toString)
      case _ => complete(StatusCodes.InternalServerError,"")
    }
  }

  def createEntity[T](creationFunc: (T) => Future[UUID])(implicit fromRequest: FromRequestUnmarshaller[T])  : Route = {
    post {
      entity(as[T]) { entity =>
         createdOr500(creationFunc(entity))
      }
    }
  }

  def fetchEntity[T](entityAccess: (UUID) => Future[Option[T]])(implicit marshaller: ToEntityMarshaller[T]) : Route = {
    path(JavaUUID) { entityId =>
      get {
        someOr404(entityAccess(entityId))
      }
    }
  }


  def withRequestContext : Directive1[UniqueLoggingContext] = new Directive1[UniqueLoggingContext] {
    override def tapply(f: (Tuple1[UniqueLoggingContext]) => Route): Route = {
      optionalHeaderValueByName("X-Request-ID") { header =>
        val context = Try{
          header.map(UUID.fromString)
        } match {
          case Success(Some(uuid)) => UniqueLoggingContext(uuid)
          case _ => UniqueLoggingContext(UUID.randomUUID())
        }
        f(Tuple1(context))
      }
    }
  }

}
