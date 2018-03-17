name := "groups"
organization := "com.notnamed.groups"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
mainClass in (Compile,run) := Some("com.notnamed.groups.Groups")

lazy val commons = RootProject(file("../commons"))
val circeVersion = "0.9.0"

resolvers += Resolver.bintrayRepo("kamon-io", "snapshots")

libraryDependencies ++= {
  Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.kamon" %% "kamon-core" % "1.0.0",
    "io.kamon" %% "kamon-prometheus" % "1.0.0",
    "io.kamon" %% "kamon-scala-future" % "1.0.0",
    "io.kamon" %% "kamon-akka-http-2.5" % "1.0.1",
    "io.kamon" %% "kamon-logback" % "1.0.0",
    "ch.qos.logback"  %   "logback-classic" % "1.2.3"
  )
}
val groups = project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

