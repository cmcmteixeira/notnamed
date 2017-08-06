name := "payments"
organization := "com.notnamed.payments"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val payments =
  project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

