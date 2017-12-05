package com.notnamed.commons.database

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.model.EventAction.EventAction

trait KeyedEntity {
  def id: UUID
  def createdOn: Timestamp
  def updatedOn: Timestamp
}
