package com.notnamed.groups.database.dao

import java.util.UUID

import com.notnamed.commons.database.BaseDao
import com.notnamed.commons.time.TimeProvider
import com.notnamed.groups.database.dao.GroupDao.GroupFilters
import com.notnamed.groups.database.entity.Group
import com.notnamed.groups.database.table.Groups

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

object GroupDao {
  case class GroupFilters(
                           id: Option[UUID],
                           owner: Option[UUID] = None,
                           active: Option[Boolean] = None,
                           user: Option[List[UUID]] = None
                         )
}

class GroupDao(db: Database,uuidGen : () => UUID)(implicit ec: ExecutionContext, timeProvider: TimeProvider) extends BaseDao[Groups,Group]{
  val table = TableQuery[Groups]

  def createGroup(group: Group) : Future[UUID]= db.run {
    table += group
  }.map(insertionCheck(group))
    .map(_ => group.id)

  def findGroupById(id: UUID) : Future[Option[Group]]= db.run {
    withGroupFilters(id = Some(id)).result
  }.map(_.headOption)


  private def withGroupFilters(
                                id: Option[UUID] = None,
                                owner: Option[UUID] = None,
                                active: Option[Boolean] = None
                              ) = baseFilterApplier(
    id.map( id => (g: BaseDaoQuery) => g.filter(_.id === id)),
    owner.map(owner => (groups: BaseDaoQuery) => groups.filter(_.owner === owner)),
    active.map {
      case true => (groups: BaseDaoQuery) => groups.filter(_.deletedOn.isEmpty)
      case false => (groups: BaseDaoQuery) => groups.filter(_.deletedOn.isDefined)
    }
  )
}
