package com.notnamed.groups

import com.notnamed.commons.kafka.{EventId}
import com.typesafe.config.ConfigFactory

object Config {
  val config = ConfigFactory.load()
  val http = new {
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
  }

  val database = new {
    val url: String = config.getString("database.url")
    val user: String = config.getString("database.user")
    val password: String = config.getString("database.password")
  }

  val remotes = new {
    val user: String = config.getString("remotes.user")
  }

  val kafka = new {
    val host: String = config.getString("kafka.host")
  }

  val events = new {
    val groupCreation = EventId("com.notnamed.groups.new.group")
  }
}
