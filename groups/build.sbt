name := "groups"
organization := "com.notnamed.groups"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
mainClass in (Compile,run) := Some("com.notnamed.groups.Groups")

lazy val commons = RootProject(file("../commons"))

val groups = project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

