package com.notnamed.groups.services

import java.util.UUID

import com.notnamed.groups.database.dao.GroupDao

import scala.concurrent.{ExecutionContext, Future}

class GroupService(groupDao: GroupDao)(implicit ec: ExecutionContext) {
  def createGroupFor(name: String, owner:UUID) : Future[UUID] = groupDao.createGroupFor(name,owner)
}
