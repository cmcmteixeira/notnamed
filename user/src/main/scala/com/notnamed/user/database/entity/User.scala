package com.notnamed.user.database.entity

import com.notnamed.commons.database.entity.KeyedEntity
import com.notnamed.commons.time.TimeProvider

case class User(id: Option[Long], email: String, createdOn: Long , updatedOn: Long ) extends KeyedEntity


