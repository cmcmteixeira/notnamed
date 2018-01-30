package com.notnamed.commons.kafka

import io.circe.Decoder

object TypedKafkaEventDecoder {
  def apply(decoders: Map[EventType, Decoder[_ <: KafkaEvent]]): TypedKafkaEventDecoder = new TypedKafkaEventDecoder(decoders)
  def apply(decoders: (EventType, Decoder[_ <: KafkaEvent])*): TypedKafkaEventDecoder = new TypedKafkaEventDecoder(decoders.toMap)
}

class TypedKafkaEventDecoder(decoders: Map[EventType,Decoder[_ <: KafkaEvent]]) {
  import io.circe._
  import io.circe.generic.semiauto._
  val eventTypeDecoder = deriveDecoder[EventType]

  implicit val decoder: Decoder[KafkaEvent] = (c: HCursor) => for {
      event <- c.downField("eventType").as[EventType](eventTypeDecoder)
      value <- decoders(event)(c)
  } yield {
      value
  }
}
