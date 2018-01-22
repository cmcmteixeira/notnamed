package com.notnamed.groups.routes

import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import com.notnamed.commons.directives.CustomDirectives._
import com.notnamed.commons.logging.RequestContext
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.groups.services.GroupService
import com.notnamed.groups.services.GroupService.{GroupWithMembers, Members, NewGroup}

trait GroupRoutesProtocol extends BaseProtocol {
  implicit val newGroupFormat = jsonFormat2(NewGroup)
  implicit val memberFormat = jsonFormat2(Members)
  implicit val groupWMemberFormat = jsonFormat3(GroupWithMembers)
}

class GroupRoutes(groupService: GroupService) extends GroupRoutesProtocol {

  def routes() : Route = withRequestContext { implicit context: RequestContext =>
      pathPrefix("group") {
        fetchEntity(groupService.fetchGroup) ~ createEntity(groupService.createGroup)
      }
  }
}
