package com.notnamed.commons.logging

import org.slf4j.{LoggerFactory}
import com.typesafe.scalalogging.{Logger => TypeSafeLogger}

class Logger(typeSafeLogger: TypeSafeLogger) {
  private def buildMessage(message:String,loggingContext: TraceContext) : String = {
    s"${loggingContext.formatted} $message"
  }

  def debug(message: String,cause: Throwable)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.debug(buildMessage(message,loggingContext),cause)
  }
  def debug(message: String)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.debug(buildMessage(message,loggingContext))
  }
  def info(message: String,cause: Throwable)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.info(buildMessage(message,loggingContext),cause)
  }
  def info(message: String)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.info(buildMessage(message,loggingContext))
  }
  def error(message: String,cause: Throwable)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.error(buildMessage(message,loggingContext),cause)
  }
  def error(message: String)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.error(buildMessage(message,loggingContext))
  }
  def warn(message: String,cause: Throwable)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.warn(buildMessage(message,loggingContext),cause)
  }
  def warn(message: String)(implicit loggingContext: TraceContext): Unit = {
    typeSafeLogger.warn(buildMessage(message,loggingContext))
  }
}

trait ContextualLogger {
  private val typesafeLogger: TypeSafeLogger = TypeSafeLogger.apply(LoggerFactory.getLogger(getClass.getName))
  val logger: Logger = new Logger(typesafeLogger)
}
