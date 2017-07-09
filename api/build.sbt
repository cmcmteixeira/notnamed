name := "api"
organization := "com.notnamed.api"
version := "1.0"
scalaVersion := "2.12.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val api =
  project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

