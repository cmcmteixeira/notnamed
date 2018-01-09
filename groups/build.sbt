name := "groups"
organization := "com.notnamed.groups"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val groups = project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

