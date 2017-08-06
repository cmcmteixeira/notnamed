name := "ledger"
organization := "com.notnamed.ledger"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val ledger =
  project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

