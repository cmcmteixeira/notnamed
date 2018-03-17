package com.notnamed.commons.directives

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import com.typesafe.scalalogging.StrictLogging
import kamon.akka.http.TracingDirectives

import scala.util.control.NonFatal

object CustomDirectives extends CustomDirectives

trait CustomDirectives extends TracingDirectives with StrictLogging {
  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server._

  def withRequestLogging: Directive0 = extractRequestContext.flatMap { ctx ⇒
    logger.info(s"${ctx.request.method.value} ${ctx.request.uri} received")
    mapRouteResult { result =>
      result match {
        case Complete(response) => logger.info(s"${ctx.request.method} ${ctx.request.uri} completed with status code ${response.status.value}")
        case Rejected(_) => logger.error(s"${ctx.request.method} ${ctx.request.uri} rejected")
      }
      result
    }
  }

  def withExceptionHandling: Directive0 = handleExceptions( ExceptionHandler {
    case NonFatal(e) =>
      logger.error("Error while processing request", e)
      complete(StatusCodes.InternalServerError,"Internal server error")
  })
}
