package com.notnamed.commons.kafka

import java.sql.Timestamp

case class EventMetadata(createdAt: Timestamp, createdBy: String, traceId: String)

case class EventId(identifier: String)
trait KafkaEvent {
  def identifier: EventId
}
case class WrappedKafkaEvent[T <: KafkaEvent](event: T, meta: EventMetadata)

