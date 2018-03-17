package com.notnamed.commons.kafka

import com.notnamed.commons.formats.DefaultJsonFormats
import io.circe.Decoder

object TypedKafkaEventDecoder {
  def apply(decoders: Map[EventId, Decoder[_ <: KafkaEvent]]): TypedKafkaEventDecoder = new TypedKafkaEventDecoder(decoders)
}

class TypedKafkaEventDecoder(decoders: Map[EventId,Decoder[_ <: KafkaEvent]]) extends DefaultJsonFormats {
  import io.circe._
  import io.circe.generic.semiauto._

  implicit val decoder: Decoder[WrappedKafkaEvent[KafkaEvent]] = (c: HCursor) => for {
      eventName <- c.downField("event").downField("eventName").as[EventId](deriveDecoder)
      event <- decoders(eventName)(c)
      metadata <- c.downField("meta").as[EventMetadata](deriveDecoder)
  } yield {
      WrappedKafkaEvent(event,metadata)
  }
}
