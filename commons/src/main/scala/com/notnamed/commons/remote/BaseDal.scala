package com.notnamed.commons.remote

import com.notnamed.commons.logging.UniqueLoggingContext

object BaseDal {
  case class RemoteRequestException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)
}
trait BaseDal {
  def sttpWContext(implicit context: UniqueLoggingContext) = com.softwaremill.sttp.sttp
      .header("X-Request-ID",context.identifier.toString)
}
