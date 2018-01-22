package com.notnamed.commons.kafka


import akka.Done
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}



object KafkaTopicProducer extends DefaultJsonProtocol {
}

class KafkaTopicProducer(topic: String)(
  settings: ProducerSettings[String,String],
  producer: KafkaProducer[String, String]
)(implicit ec: ExecutionContext, materializer: Materializer){
  import spray.json._

  def send[A](message: ActionEvent[A])(implicit format: RootJsonFormat[ActionEvent[A]]): Future[Done] = {
    Source
      .single(message.toJson.toString())
      .map(elem => new ProducerRecord[String,String](topic,elem))
      .runWith(Producer.plainSink(settings,producer))
  }
}
