package com.notnamed.commons.formats

import java.sql.Timestamp

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

trait DefaultJsonFormats {
  implicit val TimestampFormat : Encoder[Timestamp] with Decoder[Timestamp] = new Encoder[Timestamp] with Decoder[Timestamp] {
    override def apply(a: Timestamp): Json = Encoder.encodeString.apply(a.toString)

    override def apply(c: HCursor): Result[Timestamp] = Decoder.decodeString.map(Timestamp.valueOf).apply(c)
  }

  def enumEncoder[E <: Enumeration] = new Encoder[E#Value] {
    override def apply(a: E#Value) = Json.fromString(a.toString)
  }

  def enumDecoder[E <: Enumeration](enum: E) = new Decoder[E#Value] {
    override def apply(c: HCursor) = Decoder.decodeString.map(name => enum.withName(name)).apply(c)
  }
}
