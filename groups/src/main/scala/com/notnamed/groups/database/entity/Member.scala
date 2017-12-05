package com.notnamed.groups.database.entity

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.database.KeyedEntity
import com.notnamed.commons.model.EventAction.EventAction

case class Member(id: UUID,
                  userId: UUID,
                  groupId: UUID,
                  deletedOn: Option[Timestamp],
                  createdOn: Timestamp,
                  updatedOn: Timestamp
) extends KeyedEntity
