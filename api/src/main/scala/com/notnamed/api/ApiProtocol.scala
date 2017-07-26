package com.notnamed.api

import com.notnamed.api.database.entity.User
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol._
import spray.json.DefaultJsonProtocol

trait ApiProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat3(User)
}
