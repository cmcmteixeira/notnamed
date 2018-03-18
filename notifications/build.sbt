name := "notifications"
organization := "com.notnamed.notifications"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val notifications =
  project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)
val circeVersion = "0.9.0"

libraryDependencies ++= {
  Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.kamon" %% "kamon-core" % "1.0.0",
    "io.kamon" %% "kamon-prometheus" % "1.0.0",
    "io.kamon" %% "kamon-scala-future" % "1.0.0",
    "io.kamon" %% "kamon-akka-http-2.5" % "1.0.1",
    "io.kamon" %% "kamon-akka-2.5" % "1.0.0",
    "io.kamon" %% "kamon-logback" % "1.0.0",
    "ch.qos.logback"  %   "logback-classic" % "1.2.3"
  )
}