package com.notnamed.notifications

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.kafka.ConsumerSettings
import akka.stream.ActorMaterializer
import com.notnamed.commons.kafka._
import com.notnamed.notifications.groups.{GroupConsumer, GroupNotificationService}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Notifications {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val logger: LoggingAdapter = Logging(system, getClass)


  def main(args: Array[String]): Unit = {

    implicit val ec: ExecutionContext = ExecutionContext.global
    val consumerSettings: ConsumerSettings[String, String] = ConsumerSettings(
      Config.config.getConfig("akka.kafka.consumer"),
      new StringDeserializer,
      new StringDeserializer
    ).withGroupId("com.notnamed.notifications")

    val groupEventsConsumer = new KafkaTopicConsumer(100, "groups")(
      consumerSettings,
      consumerSettings.createKafkaConsumer()
    )

    val groupNotificationService = new GroupNotificationService

    val groupConsumer = new GroupConsumer(
      groupEventsConsumer,
      groupNotificationService
    )
    groupConsumer.consume
  }
}
