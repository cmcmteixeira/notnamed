package com.notnamed.commons.remote

import com.notnamed.commons.logging.RequestContext

object BaseDal {
  case class RemoteRequestException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)
}
trait BaseDal {
  def sttpWContext(implicit context: RequestContext) = com.softwaremill.sttp.sttp
      .header("X-Request-ID",context.requestId.toString)
}
