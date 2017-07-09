package com.notnamed.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.notnamed.api.database.entity.User
import spray.json.DefaultJsonProtocol


object Protocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val UserFormat = jsonFormat3(User)
}
