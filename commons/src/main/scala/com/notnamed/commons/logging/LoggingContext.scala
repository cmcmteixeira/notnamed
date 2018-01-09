package com.notnamed.commons.logging

import java.util.UUID

sealed trait LoggingContext {

}


case class RequestContext(requestId: UUID) extends LoggingContext
