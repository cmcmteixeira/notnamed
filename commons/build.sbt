name := "commons"
organization := "com"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val akkaV       = "2.5.6"
val akkaHttpV   = "10.0.10"
val scalaTestV  = "3.0.1"
val circeVersion = "0.9.0"
libraryDependencies ++= {
  Seq(
    "com.softwaremill.sttp" %% "core" % "1.1.4",
    "com.softwaremill.sttp" %% "akka-http-backend" % "1.1.4",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.19",
    "com.typesafe.slick" %% "slick" % "3.2.0",
    "com.typesafe" % "config" % "1.3.1",
    "mysql" % "mysql-connector-java" % "5.1.12",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
    "org.scala-lang" % "scala-reflect" % "2.12.1",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "org.mockito" % "mockito-core" % "1.8.5" % "test",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.kamon" %% "kamon-core" % "1.0.0",
    "io.kamon" %% "kamon-akka-http-2.5" % "1.0.1",
    "org.flywaydb" % "flyway-core" % "5.0.7"
  )
}

val commons = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings : _*)
