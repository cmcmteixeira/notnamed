package com.notnamed.commons.database.table

import slick.lifted.Rep


trait KeyedTable {
  def id : Rep[Int]
}
