package com.notnamed.groups.database.dao

import java.util.UUID

import com.notnamed.commons.database.KeyedDao
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.entity.Group
import com.notnamed.groups.database.table.Groups

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

class GroupDao(override val db: Database, val table : TableQuery[Groups])
              (implicit uuidGen : () => UUID, val ec: ExecutionContext, timeProvider: TimeProvider)
              extends KeyedDao[Groups,Group]{

  def createGroupFor(name: String, owner: UUID) : Future[UUID]= insert(
    Group(uuidGen(),
      name,
      updatedOn = timeProvider.now(),
      timeProvider.now()
    )
  )
}
