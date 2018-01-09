package com.notnamed.commons.protocol

import java.sql.Timestamp
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsString, JsValue, JsonFormat}

trait BaseProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)
    def read(value: JsValue) = {
      value match {
        case JsString(uuid) => UUID.fromString(uuid)
        case _              => throw DeserializationException("Expected hexadecimal UUID string")
      }
    }
  }

  implicit object TimestampFormat extends JsonFormat[Timestamp] {

    import java.text.SimpleDateFormat

    private val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def write(obj: Timestamp) = JsString(sdf.format(obj))
    def read(json: JsValue) = json match {
      case JsString(time) => new Timestamp(sdf.parse(time).toInstant.getEpochSecond)
      case _ => throw DeserializationException("Date expected")
    }
  }
}
