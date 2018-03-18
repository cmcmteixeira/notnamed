package com.notnamed.commons.kafka


import akka.Done
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.notnamed.commons.formats.DefaultJsonFormats
import com.notnamed.commons.time.TimeProvider
import kamon.Kamon
import kamon.trace.Span
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.{ExecutionContext, Future}


class KafkaTopicProducer(topic: String)(
  timeProvider: TimeProvider,
  producerId: String,
  settings: ProducerSettings[String,String],
  producer: KafkaProducer[String, String]
)(implicit ec: ExecutionContext, materializer: Materializer) extends DefaultJsonFormats{
  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.syntax._


  implicit def actionEventEncoder[A <: KafkaEvent](implicit enc: Encoder[A]): Encoder[WrappedKafkaEvent[A]] = (a: WrappedKafkaEvent[A]) => Json.obj(
    ("meta", a.meta.asJson(deriveEncoder)),
    ("event", a.event.asJson(enc))
  )

  def send[A <: KafkaEvent](message: A)(implicit format: Encoder[A]): Future[Done] = {
    Source
      .single(WrappedKafkaEvent(message, EventMetadata(
        createdAt = timeProvider.now(),
        createdBy = producerId,
        traceId = Kamon.currentContext().get(Span.ContextKey).context().traceID.string
      )))
      .map(elem =>
        new ProducerRecord[String,String](topic, elem.asJson(actionEventEncoder(format)).toString())
      )
      .runWith(Producer.plainSink(settings,producer))
  }
}

