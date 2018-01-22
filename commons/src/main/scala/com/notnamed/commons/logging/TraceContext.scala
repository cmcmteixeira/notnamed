package com.notnamed.commons.logging

import java.util.UUID

sealed trait TraceContext {
  def formatted: String
}

case class RequestContext(requestId: UUID) extends TraceContext {
  override def formatted = s"[req:$requestId]"

}
