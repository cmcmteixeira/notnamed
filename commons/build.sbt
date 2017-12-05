name := "commons"
organization := "com"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val akkaV       = "2.5.6"
val akkaHttpV   = "10.0.10"
val scalaTestV  = "3.0.1"
libraryDependencies ++= {
  Seq(
    "com.softwaremill.sttp" %% "core" % "0.0.2",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % akkaV ,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
    "com.typesafe.slick" %% "slick" % "3.2.0",
    "com.typesafe" % "config" % "1.3.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "mysql" % "mysql-connector-java" % "5.1.12",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
    "org.scala-lang" % "scala-reflect" % "2.12.1",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "org.mockito" % "mockito-core" % "1.8.5" % "test"
  )
}

project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings : _*)
