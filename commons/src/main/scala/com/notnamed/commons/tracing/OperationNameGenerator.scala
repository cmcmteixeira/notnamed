package com.notnamed.commons.tracing
import akka.http.scaladsl.model.HttpRequest

class OperationNameGenerator extends kamon.akka.http.AkkaHttp.OperationNameGenerator {
  override def serverOperationName(request: HttpRequest): String = {
    request.uri.path.toString().replaceAll("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",":uuid")
  }

  override def clientOperationName(request: HttpRequest): String = {
    request.uri.path.toString()
  }
}
