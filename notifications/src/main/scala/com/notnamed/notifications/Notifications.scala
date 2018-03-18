package com.notnamed.notifications

import java.time.Clock

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.kafka.ConsumerSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import com.notnamed.commons.kafka._
import com.notnamed.commons.time.TimeProvider
import com.notnamed.notifications.groups.{GroupConsumer, GroupNotificationService}
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object Notifications {


  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val executor: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val logger: LoggingAdapter = Logging(system, getClass)


    implicit val sttpBackend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.usingActorSystem(system)
    val clock : Clock = Clock.systemUTC()
    implicit val timeProvider: TimeProvider = new TimeProvider(clock)
    implicit val ec: ExecutionContext = ExecutionContext.global


    val consumerSettings: ConsumerSettings[String, String] = ConsumerSettings(
      Config.config.getConfig("akka.kafka.consumer"),
      new StringDeserializer,
      new StringDeserializer
    ).withGroupId("com.notnamed.notifications")

    val groupEventsConsumer = new KafkaTopicConsumer(100, "groups")(
      consumerSettings,
      consumerSettings.createKafkaConsumer()
    )(ec)

    val groupNotificationService = new GroupNotificationService

    val groupConsumer = new GroupConsumer(
      groupEventsConsumer,
      groupNotificationService
    )

    Http().bindAndHandle(
      routes,
      Config.http.interface,
      Config.http.port
    )
    groupConsumer.getConsumer.runWith(Sink.ignore)
  }

  def routes = (get & path("version")) {
    complete("meh")
  }


}
