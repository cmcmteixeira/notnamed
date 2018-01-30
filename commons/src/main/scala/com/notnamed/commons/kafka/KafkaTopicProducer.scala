package com.notnamed.commons.kafka


import akka.Done
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.notnamed.commons.formats.DefaultJsonFormats
import io.circe.Encoder
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.{ExecutionContext, Future}


object KafkaTopicProducer extends DefaultJsonFormats {
  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.syntax._



  implicit def actionEventEncoder[A](implicit enc: Encoder[A]): Encoder[KafkaDetailedEvent[A]] = (a: KafkaDetailedEvent[A]) => Json.obj(
    ("eventType", a.eventType.asJson(deriveEncoder)),
    ("meta", a.meta.asJson(deriveEncoder)),
    ("details", a.details.asJson(enc))
  )

}

class KafkaTopicProducer(topic: String)(
  settings: ProducerSettings[String,String],
  producer: KafkaProducer[String, String]
)(implicit ec: ExecutionContext, materializer: Materializer){
  import KafkaTopicProducer._
  import io.circe.syntax._

  def send[A](message: KafkaDetailedEvent[A])(implicit format: Encoder[A]): Future[Done] = {
    Source
      .single(message.asJson(actionEventEncoder).toString)
      .map(elem => new ProducerRecord[String,String](topic,elem))
      .runWith(Producer.plainSink(settings,producer))
  }
}

