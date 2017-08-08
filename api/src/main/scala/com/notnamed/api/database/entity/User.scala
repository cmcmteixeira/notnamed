package com.notnamed.api.database.entity

import com.notnamed.commons.database.entity.KeyedEntity


case class User(id: Option[Long], email: String,password: String) extends KeyedEntity
