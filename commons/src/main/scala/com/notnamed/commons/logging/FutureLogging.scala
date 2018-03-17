package com.notnamed.commons.logging

import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}


object FutureLogging extends StrictLogging {
  def logFutureFailure : PartialFunction[Try[_],Unit] = {
    case Failure(e) => logger.warn(e.getMessage,e)
    case Success(_) => ()
  }
}
