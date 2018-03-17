package com.notnamed.groups.routes

import akka.event.Logging
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.groups.services.GroupService
import com.notnamed.groups.services.GroupService.{GroupWithMembers, Members, NewGroup}

import scala.concurrent.ExecutionContext

trait GroupRoutesProtocol extends BaseProtocol  {
  implicit val newGroupFormat = jsonFormat2(NewGroup)
  implicit val memberFormat = jsonFormat2(Members)
  implicit val groupWMemberFormat = jsonFormat3(GroupWithMembers)
}

class GroupRoutes(groupService: GroupService) extends GroupRoutesProtocol {
  import akka.http.scaladsl.server.Directives.{as, _}
  import akka.http.scaladsl.server._
  import com.notnamed.commons.directives.CustomDirectives._


  def routes()(implicit ec: ExecutionContext): Route = (withRequestLogging & withExceptionHandling ) {
    pathPrefix("group") {
      (get & path(JavaUUID) & rejectEmptyResponse) { uuid =>
        complete(groupService.fetchGroup(uuid))
      } ~
        (post & entity(as[NewGroup])) { entity =>
          complete(groupService.createGroup(entity).map(_.toString))
        }
    }
  }

}
