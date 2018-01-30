package com.notnamed.notifications.groups

import java.util.UUID

import com.notnamed.commons.formats.DefaultJsonFormats
import com.notnamed.commons.kafka.{EventMetadata, EventType, KafkaEvent}
import io.circe.generic.semiauto.deriveDecoder

object Events {
  case class NewGroupEventDetails(id: UUID,createdBy: UUID, name: String)
  case class NewGroupEvent(override val eventType: EventType, override val meta: EventMetadata, details: NewGroupEventDetails) extends KafkaEvent
  case class DeleteGroupEvent(override val eventType: EventType, override val meta: EventMetadata, details: NewGroupEventDetails) extends KafkaEvent

  object Decoders extends DefaultJsonFormats {
    private implicit val eventType = deriveDecoder[EventType]
    private implicit val enventMeta = deriveDecoder[EventMetadata]
    private implicit val newGroupEventDetails = deriveDecoder[NewGroupEventDetails]
    implicit val newGroupEvent = deriveDecoder[NewGroupEvent]
  }
}
