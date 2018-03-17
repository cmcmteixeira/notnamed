package com.notnamed.commons.tracing

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import kamon.Kamon
import kamon.trace.{IdentityProvider, Span}

class LogbackSpanIDConverter extends ClassicConverter{
  override def convert(event: ILoggingEvent): String = {
    val currentSpan = Kamon.currentContext().get(Span.ContextKey)
    val spanId = currentSpan.context().spanID
    if(spanId == IdentityProvider.NoIdentifier)
      "undefined"
    else
      spanId.string
  }
}
