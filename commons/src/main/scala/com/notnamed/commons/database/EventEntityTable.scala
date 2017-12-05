package com.notnamed.commons.database

import java.sql.Timestamp
import java.util.UUID

import slick.jdbc.MySQLProfile.api._

trait EventEntityTable {
  def id: Rep[UUID]
  def createdOn: Rep[Timestamp]
  def updatedOn: Rep[Timestamp]
}
