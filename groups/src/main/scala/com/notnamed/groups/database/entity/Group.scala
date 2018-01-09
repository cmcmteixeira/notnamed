package com.notnamed.groups.database.entity

import java.util.UUID

import com.notnamed.commons.entity.{AuditInfo, AuditUUIDEntity, UUIDEntity}

case class Group(
                  id: UUID,
                  name: String,
                  createdBy: UUID,
                  audit: AuditInfo) extends AuditUUIDEntity