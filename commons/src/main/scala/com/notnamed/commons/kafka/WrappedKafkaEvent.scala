package com.notnamed.commons.kafka

import java.sql.Timestamp

case class EventMetadata(createdAt: Timestamp, createdBy: String, traceId: String)

case class EventId(id: String)
trait KafkaEvent {
  def eventId: EventId
}
case class WrappedKafkaEvent[T <: KafkaEvent](event: T, meta: EventMetadata)

