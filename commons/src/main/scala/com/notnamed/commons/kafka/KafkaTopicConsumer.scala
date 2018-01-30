package com.notnamed.commons.kafka

import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.notnamed.commons.formats.DefaultJsonFormats
import com.notnamed.commons.logging.{EmptyLoggingContext, ContextualLogger, UniqueLoggingContext}
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.concurrent.{ExecutionContext, Future}




object KafkaTopicConsumer extends DefaultJsonFormats {

}

class KafkaTopicConsumer(parallelism: Int,topics: String*)(settings: ConsumerSettings[String,String], producer: KafkaConsumer[String, String])(implicit ec: ExecutionContext,materializer: Materializer) extends ContextualLogger  {
  import io.circe.parser._
  def startConsumer[_ <: KafkaEvent](decoder: TypedKafkaEventDecoder)(handler: PartialFunction[KafkaEvent, Future[_]]): Future[_] = Consumer
      .committableSource(settings,Subscriptions.topics(topics.toSet))
      .mapAsync(parallelism) { record =>
        Future((
          parse(record.record.value()).flatMap(_.as[KafkaEvent](decoder.decoder)),
          record
        ))
      }
      .mapAsync(parallelism) {
        case (Right(event), record) =>
          logger.info(s"Processing event of type: ${event.eventType} and offset ${record.committableOffset.partitionOffset.offset}.")(UniqueLoggingContext(event.meta.identifier))
          handler(event)
          .map{ _ =>
            logger.info(s"Successfully processed event of type: ${event.eventType} and offset ${record.committableOffset.partitionOffset.offset}.")(UniqueLoggingContext(event.meta.identifier))
            record.committableOffset
          }
        case (Left(exception),record) =>
          logger.error(s"Failed to serialize event: ${record.record.value()}.",exception)(EmptyLoggingContext())
          Future.failed(exception)
      }
      .batch(parallelism, first => CommittableOffsetBatch.empty.updated(first)) { (batch,elem) =>
        batch.updated(elem)
      }
      .mapAsync(parallelism)(_.commitScaladsl())
      .runWith(Sink.ignore)
}
