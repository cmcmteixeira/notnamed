package com.notnamed.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.notnamed.api.database.entity.User
import spray.json.DefaultJsonProtocol


trait ApiProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat3(User)
}
