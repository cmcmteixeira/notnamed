name := "user"
organization := "com.notnamed.user"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val commons = RootProject(file("../commons"))
val user =
  project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "2.+" % "test"
