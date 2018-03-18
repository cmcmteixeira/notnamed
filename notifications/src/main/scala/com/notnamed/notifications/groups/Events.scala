package com.notnamed.notifications.groups

import java.util.UUID

import com.notnamed.commons.formats.DefaultJsonFormats
import com.notnamed.commons.kafka._
import io.circe.generic.semiauto.deriveDecoder

object Events {
  case class NewGroupEvent(eventId: EventId, groupId: UUID) extends KafkaEvent

  object Decoders extends DefaultJsonFormats {
    private implicit val eventType = deriveDecoder[EventId]
    private implicit val newGroupEventDetails = deriveDecoder[NewGroupEvent]
    implicit val newGroupEvent = deriveDecoder[NewGroupEvent]
  }
}
