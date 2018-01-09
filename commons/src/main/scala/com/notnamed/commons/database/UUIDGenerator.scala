package com.notnamed.commons.database

import java.util.UUID

object UUIDGenerator extends UUIDGenerator

trait UUIDGenerator {
  def genUUID() = UUID.randomUUID()
}