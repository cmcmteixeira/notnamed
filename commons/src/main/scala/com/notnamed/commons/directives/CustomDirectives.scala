package com.notnamed.commons.directives

import java.util.UUID

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import com.notnamed.commons.logging.{LoggingContext, RequestContext}

import scala.concurrent.Future
import scala.util.{Failure, Success}

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
      case Success(_) => complete(StatusCodes.Created,"")
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

  def withRequestContext(directives : (LoggingContext) => Route) : Route = {
    directives(RequestContext(UUID.randomUUID()))
  }
}
