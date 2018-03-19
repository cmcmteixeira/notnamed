package com.notnamed.groups.routes

import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.groups.services.GroupService
import com.notnamed.groups.services.GroupService.{GroupWithMembers, Members, NewGroup, NewGroupMember}

import scala.concurrent.ExecutionContext

trait GroupRoutesProtocol extends BaseProtocol  {
  implicit val newGroupFormat = jsonFormat2(NewGroup)
  implicit val memberFormat = jsonFormat2(Members)
  implicit val groupWMemberFormat = jsonFormat3(GroupWithMembers)
  implicit val newGroupWMemberFormat = jsonFormat1(NewGroupMember)
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
      } ~
      (post & path(JavaUUID / "members") & entity(as[NewGroupMember])) { (groupId, newMember) =>
        complete(groupService.addMember(groupId)(newMember).map(_.toString))
      }
    }
  }

}
