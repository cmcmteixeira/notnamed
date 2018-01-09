package com.notnamed.user.database.entity

import java.util.UUID

import com.notnamed.commons.entity.{AuditUUIDEntity, AuditInfo}

case class User(id: UUID, email: String, audit: AuditInfo ) extends AuditUUIDEntity


