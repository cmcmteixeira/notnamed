package com.notnamed.groups.database.entity

import java.util.UUID

import com.notnamed.commons.entity.{AuditInfo, AuditUUIDEntity}

case class Membership(id: UUID,
                      userId: UUID,
                      groupId: UUID,
                      audit: AuditInfo
) extends AuditUUIDEntity
