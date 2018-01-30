package com.notnamed.groups.dal

import java.util.UUID

import com.notnamed.commons.logging.{ContextualLogger, UniqueLoggingContext}
import com.notnamed.commons.protocol.BaseProtocol
import com.notnamed.commons.remote.BaseDal
import com.softwaremill.sttp.SttpBackend

import scala.concurrent.{ExecutionContext, Future}

object UserDal extends BaseProtocol {
  def fetchUserPath = s"user"
  case class User(id: UUID)
  implicit val userIdFormat = jsonFormat1(User)
}
class UserDal(basePath: String)(implicit akkaHttpBackend: SttpBackend[Future,_],ec: ExecutionContext) extends BaseDal with ContextualLogger {
  import UserDal._
  import spray.json._
  import com.softwaremill.sttp._
  def fetchUserInfo(userId: UUID)(implicit context: UniqueLoggingContext) : Future[User] = sttpWContext
      .get(uri"$basePath/user/$userId")
      .send()
      .map{
        case Response(Right(user),200,_,_,_) => user.parseJson.convertTo[User]
        case Response(response,_,_,_,_) => throw BaseDal.RemoteRequestException(s"Request to /user/$userId returned an unexpected result [response:$response].")
      }

}
