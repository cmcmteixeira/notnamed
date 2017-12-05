package com.notnamed.commons.model


object EventAction extends Enumeration {
  type EventAction = Value
  val CREATE = Value("create")
  val DELETE = Value("delete")
  val UPDATE = Value("update")
}
