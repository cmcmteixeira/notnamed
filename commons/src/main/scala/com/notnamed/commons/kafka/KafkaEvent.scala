package com.notnamed.commons.kafka

import java.sql.Timestamp
import java.util.UUID

case class EventType(id: String)
case class EventMetadata(createdAt: Timestamp, createdBy: String, identifier: UUID)

trait KafkaEvent {
  def eventType: EventType
  def meta: EventMetadata
}
case class KafkaDetailedEvent[A](eventType: EventType, meta: EventMetadata, details: A) extends KafkaEvent

