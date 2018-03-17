package com.notnamed.commons.remote


object BaseDal {
  case class RemoteRequestException(description: String, cause: Exception = None.orNull) extends Throwable(description,cause)
}
