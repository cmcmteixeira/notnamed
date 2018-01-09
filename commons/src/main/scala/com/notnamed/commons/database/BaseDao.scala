package com.notnamed.commons.database

import com.notnamed.commons.database.BaseDao.KeyedDaoUnexpectedValueException
import com.notnamed.commons.entity.AuditUUIDEntity
import slick.lifted.{AbstractTable, Query, TableQuery}

object BaseDao {
  case class KeyedDaoUnexpectedValueException(message: String,cause: Throwable = None.orNull) extends Throwable(message,cause)
}

trait BaseDao[T <: AbstractTable[_], E <: AuditUUIDEntity]  {
  type BaseDaoQuery = Query[T,T#TableElementType,Seq]
  def table : TableQuery[T] with Query[T,T#TableElementType,Seq]

  protected def singleUpdateCheck(entity: E)(affectedRows: Int): Unit = affectedRows match {
    case 1 => ()
    case result => throw KeyedDaoUnexpectedValueException(s"Unexpected value when trying to update entity with [uuid:${entity.id}]. Expected result to be 0 but got $result.")
  }

  protected def singleUpdateCheck(affectedRows: Int): Unit = affectedRows match {
    case 1 => ()
    case result => throw KeyedDaoUnexpectedValueException(s"Unexpected value when trying to update entity. Expected result to be 0 but got $result.")
  }

  protected def insertionCheck(entity: E)(affectedRows: Int): Unit = affectedRows match {
    case 1 => ()
    case result => throw KeyedDaoUnexpectedValueException(s"Unexpected value when trying to insert entity with [uuid:${entity.id}]. Expected result to be 0 but got $result.")
  }

  protected def baseFilterApplier(filters: Option[BaseDaoQuery => BaseDaoQuery]*) : Query[T,T#TableElementType,Seq] =
    filters
      .flatten
      .foldLeft[BaseDaoQuery](table)((acc,filter) => filter(acc))
}

