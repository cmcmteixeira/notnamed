package com.notnamed.commons.database.table
import slick.jdbc.MySQLProfile.api._

trait KeyedTable {
  def id : Rep[Long]
}
