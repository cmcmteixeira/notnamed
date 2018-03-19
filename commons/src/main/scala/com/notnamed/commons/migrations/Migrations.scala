package com.notnamed.commons.migrations

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FlywayConfiguration

object Migrations {
  def run = {
    val config = ConfigFactory.load()
    val flyway: Flyway = new Flyway
    flyway.setDataSource(
      config.getString("database.url"),
      config.getString("database.user"),
      config.getString("database.password"))
    flyway.setBaselineOnMigrate(true)
    flyway.migrate()
  }
}
