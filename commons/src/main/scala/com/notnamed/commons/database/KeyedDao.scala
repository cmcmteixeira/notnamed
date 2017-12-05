package com.notnamed.commons.database

import java.util.UUID

import com.notnamed.commons.database.KeyedDao.KeyedDaoUnexpectedValueException
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object KeyedDao {
  case class KeyedDaoUnexpectedValueException(message: String,cause: Throwable = None.orNull) extends Throwable(message,cause)
}

trait KeyedDao[T <: Table[Entity] with EventEntityTable ,Entity <: KeyedEntity]  {
  def table : TableQuery[T]
  def db : Database
  implicit def ec: ExecutionContext

  def findBy(uuid: UUID) : Future[Option[Entity]] = findBy(Seq(uuid))
    .map{
      case head :: Nil => Some(head)
      case Nil => None
      case _ => throw KeyedDaoUnexpectedValueException(s"Find by uuid expects one result but more than one results were returned for entity with [uuid:$uuid].")
    }

  def findBy(uuids: Seq[UUID]) : Future[Iterable[Entity]] = db.run {
    table.filter(entity => entity.id === uuids.head).result
  }


  protected def insert(entity: Entity) : Future[UUID] = db.run {
    table += entity
  }.map(insertionCheck(entity))
    .map(_ => entity.id)


  protected def singleUpdateCheck(affectedRows: Int) = affectedRows match {
    case 1 => ()
    case result => throw KeyedDaoUnexpectedValueException(s"Unexpected value when trying to update entity with [uuid:${entity.id}]. Expected result to be 0 but got $result.")
  }
  protected def insertionCheck(entity: Entity)(affectedRows: Int) = affectedRows match {
    case 1 => ()
    case result => throw KeyedDaoUnexpectedValueException(s"Unexpected value when trying to insert entity with [uuid:${entity.id}]. Expected result to be 0 but got $result.")
  }
}

