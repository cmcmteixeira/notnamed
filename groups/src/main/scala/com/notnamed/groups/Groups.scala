package com.notnamed.groups

import java.time.Clock

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.notnamed.commons.kafka.KafkaTopicProducer
import com.notnamed.commons.time.TimeProvider
import com.notnamed.commons.uuid.UUIDGenerator
import com.notnamed.groups.dal.UserDal
import com.notnamed.groups.database.dao.{GroupDao, MembershipDao}
import com.notnamed.groups.routes.GroupRoutes
import com.notnamed.groups.services.GroupService
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object Groups {
  import slick.jdbc.MySQLProfile.api._
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val logger: LoggingAdapter = Logging(system, getClass)


  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    Http().bindAndHandle(
      routes,
      Config.http.interface,
      Config.http.port
    )
  }

  def routes : Route = {
    implicit val sttpBackend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.usingActorSystem(system)

    val db = Database.forConfig("database")
    implicit val clock : Clock = Clock.systemUTC()
    implicit val timeProvider: TimeProvider = new TimeProvider(clock)
    implicit val ec: ExecutionContext = ExecutionContext.global
    val uuidGenerator = new UUIDGenerator

    val producerSettings: ProducerSettings[String, String] = ProducerSettings(
      Config.config.getConfig("akka.kafka.producer"),
      new StringSerializer,
      new StringSerializer
    )

    val producer: KafkaProducer[String, String] = producerSettings
      .createKafkaProducer()
    val kafkaProducer = new KafkaTopicProducer("groups")(producerSettings,producer)

    val groupDao = new GroupDao(db)
    val memberShipDao = new MembershipDao(db)
    val userDal = new UserDal(Config.remotes.user)
    val groupService = new GroupService(
      groupDao,
      memberShipDao,userDal,
      uuidGenerator,
      kafkaProducer
    )


    new GroupRoutes(groupService).routes()
  }

}
