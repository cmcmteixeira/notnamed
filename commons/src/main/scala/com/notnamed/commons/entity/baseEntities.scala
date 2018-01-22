package com.notnamed.commons.entity

import java.sql.Timestamp
import java.util.UUID

trait UUIDEntity {
  def id: UUID
}

trait AuditUUIDEntity extends UUIDEntity {
  def audit: AuditInfo
}

case class AuditInfo(
  createdOn: Timestamp,
  updatedOn: Timestamp,
  deletedOn: Option[Timestamp] = None
)