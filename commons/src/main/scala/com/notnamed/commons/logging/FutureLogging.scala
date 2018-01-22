package com.notnamed.commons.logging

import scala.util.{Failure, Success, Try}


trait FutureLogging { this : LoggerWithContext =>
  def logFutureFailure(implicit requestContext: RequestContext) : PartialFunction[Try[_],Unit] = {
    case Failure(e) => logger.warn(e.getMessage,e)
    case Success(_) => ()
  }
}
