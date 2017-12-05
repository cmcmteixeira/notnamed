package com.notnamed.groups.database.entity

import java.sql.Timestamp
import java.util.UUID

import com.notnamed.commons.database.KeyedEntity

case class Group(id: UUID, name: String, updatedOn: Timestamp, createdOn: Timestamp) extends KeyedEntity