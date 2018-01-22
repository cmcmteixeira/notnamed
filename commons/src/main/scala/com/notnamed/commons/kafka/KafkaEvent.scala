package com.notnamed.commons.kafka

import java.sql.Timestamp

import com.notnamed.commons.kafka.Action.Action

object Action extends Enumeration {
  type Action = Value
  val Created = Value(1,"CREATE")
  val Updated = Value(2,"UPDATE")
  val Delete = Value(3,"DELETE")
}
case class MetaEventInfo(createdAt: Timestamp, createdBy: String)


sealed trait Event {
  val meta: MetaEventInfo
}

case class ActionEvent[A](action: Action, meta: MetaEventInfo, details: A) extends Event
