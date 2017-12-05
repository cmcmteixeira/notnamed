package com.notnamed.groups

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

  val kafka = new {
    val host: String = config.getString("kafka.")
  }
}
