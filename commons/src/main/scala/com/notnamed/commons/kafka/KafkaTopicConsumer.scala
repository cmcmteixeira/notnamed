package com.notnamed.commons.kafka

import akka.Done
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Source
import com.notnamed.commons.formats.DefaultJsonFormats
import com.typesafe.scalalogging.StrictLogging
import kamon.Kamon
import kamon.trace.SpanContext.SamplingDecision
import kamon.trace.{IdentityProvider, Span, SpanContext}
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.concurrent.{ExecutionContext, Future}

object KafkaTopicConsumer extends DefaultJsonFormats {

}

class KafkaTopicConsumer(parallelism: Int,topics: String*)(settings: ConsumerSettings[String,String], producer: KafkaConsumer[String, String])(implicit ec: ExecutionContext) extends StrictLogging  {
  import io.circe.parser._

  private def withKamonTracing[A](event: WrappedKafkaEvent[_ <: KafkaEvent])(f: => A) : A  = {
    val span = Span.Remote(SpanContext(
      IdentityProvider.Default().spanIdGenerator().from(event.meta.traceId),
      IdentityProvider.Default().traceIdGenerator().generate(),
      IdentityProvider.Default().traceIdGenerator().generate(),
      SamplingDecision.DoNotSample
    ))
    Kamon.withSpan(span,finishSpan = false){
      f
    }
  }



  def createConsumer[_ <: KafkaEvent](decoder: TypedKafkaEventDecoder)(handler: PartialFunction[KafkaEvent, Future[_]]): Source[Done, Consumer.Control] = Consumer
    .committableSource(settings,Subscriptions.topics(topics.toSet))
    .mapAsync(parallelism) { record =>
      Future((
        parse(record.record.value()).flatMap(_.as[WrappedKafkaEvent[KafkaEvent]](decoder.decoder)),
        record
      ))
    }
    .mapAsync(parallelism) {
      case (Right(event), record) => withKamonTracing(event){
        logger.info(s"Processing event of type: ${event.event.eventId.id} and offset ${record.committableOffset.partitionOffset.offset}.")
        handler(event.event)
          .map{ _ =>
            logger.info(s"Successfully processed event of type: ${event.event.eventId.id} and offset ${record.committableOffset.partitionOffset.offset}.")
            record.committableOffset
          }
      }
      case (Left(exception),record) =>
        logger.error(s"Failed to serialize event: ${record.record.value()}.",exception)
        Future.failed(exception)
    }
    .batch(parallelism, first => CommittableOffsetBatch.empty.updated(first)) { (batch,elem) =>
      batch.updated(elem)
    }
    .mapAsync(parallelism)(_.commitScaladsl())
}
