package com.notnamed.user.database.entity

import com.notnamed.commons.time.TimeProvider

case class User(id: Option[Int], email: String, createdOn: Long = TimeProvider.now(), updatedOn: Long = TimeProvider.now())


