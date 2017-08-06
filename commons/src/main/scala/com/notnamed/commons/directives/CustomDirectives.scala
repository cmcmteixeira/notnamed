package com.notnamed.commons.directives

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import scala.util.{Failure, Success}

object CustomDirectives extends CustomDirectives{

}
trait CustomDirectives {
  def someOr404[T](future: Future[Option[T]])(implicit marshaller: ToEntityMarshaller[T]): Route = {
    onComplete(future){
      case Success(Some(value)) => complete((StatusCodes.OK, value))
      case Success(None) => complete(StatusCodes.NotFound,"Not Found")
      case Failure(exception) => complete(StatusCodes.InternalServerError,"An error ")
    }
  }

  def createdOr500(future: Future[Long]) : Route = {
    onComplete(future){
      case Success(id) => complete(StatusCodes.Created,"")
      case _ => complete(StatusCodes.InternalServerError,"")
    }
  }
  def entityCreation[T](creationFunc: (T) => Future[Long]) : Route = {
    post {
      entity(as[T]) { entity =>
         createdOr500(creationFunc(entity))
      }
    }
  }
  def entityFetching[T](entityAccess: (Long) => Future[Option[T]])(implicit marshaller: ToEntityMarshaller[T]) : Route = {
    path(LongNumber) { entityId =>
      get {
        someOr404(entityAccess(entityId))
      }
    }
  }
}
