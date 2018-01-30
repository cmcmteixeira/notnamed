package com.notnamed.commons.logging

import java.util.UUID

sealed trait TraceContext {
  def formatted: String
}

object EmptyLoggingContext {
  def apply(): EmptyLoggingContext = new EmptyLoggingContext()
}

final class EmptyLoggingContext extends TraceContext {
  override def formatted = "[]"
}
case class UniqueLoggingContext(identifier: UUID) extends TraceContext {
  override def formatted = s"[id:$identifier]"
}
