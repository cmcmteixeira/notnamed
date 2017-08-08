package com.notnamed.commons.database.dao

import com.notnamed.commons.database.entity.KeyedEntity
import com.notnamed.commons.database.table.KeyedTable
import com.notnamed.commons.time.TimeProvider
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait KeyedDao[T <: Table[Entity] with KeyedTable ,Entity <: KeyedEntity] {
  def table : TableQuery[T]
  def db : Database
  implicit def ec: ExecutionContext

  def findById(id: Long) : Future[Option[Entity]] = db.run {
    table.filter(entity => entity.id === id).result map(_.headOption)
  }
  def findByIds(ids: Seq[Long]) : Future[Seq[Entity]] = db.run {
    table.filter(entity => entity.id inSet ids).result
  }
  def insert(entity: Entity) : Future[Long] = db.run {
    (table returning table.map(_.id)) += entity
  }
}

