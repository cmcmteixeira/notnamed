package com.notnamed.commons.logging

import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.{Logger => TypeSafeLogger}

class Logger(typeSafeLogger: TypeSafeLogger) {
  private def handleContext(loggingContext: LoggingContext) : Unit = {

  }

  def debug(message: String,cause: Throwable)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.debug(message,cause)
  }
  def debug(message: String)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.debug(message)
  }
  def info(message: String,cause: Throwable)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.info(message,cause)
  }
  def info(message: String)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.info(message)
  }
  def error(message: String,cause: Throwable)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.error(message,cause)
  }
  def error(message: String)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.error(message)
  }
  def warn(message: String,cause: Throwable)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.warn(message,cause)
  }
  def warn(message: String)(loggingContext: LoggingContext) = {
    handleContext(loggingContext)
    typeSafeLogger.warn(message)
  }
}

trait LoggerWithContext {
  private val typesafeLogger: TypeSafeLogger = TypeSafeLogger(LoggerFactory.getLogger(getClass.getName))
  val logger = new Logger(typesafeLogger)
}
