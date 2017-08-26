name := "user"
organization := "com.notnamed.user"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

enablePlugins(DockerPlugin, DockerComposePlugin)


lazy val commons = RootProject(file("../commons"))
lazy val DockerIntegrationTest = config("it") extend IntegrationTest

lazy val docker = taskKey[Unit]("IntegrationTests with Running Docker Container") in DockerIntegrationTest
docker := {
  println("Hello2")
  dockerComposeUp
}

val user =
  project.in(file("."))
    .configs(DockerIntegrationTest)
      .settings(Defaults.itSettings)
    .aggregate(commons)
    .dependsOn(commons)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "it,test"
libraryDependencies += "org.mockito" % "mockito-all" % "2.+" % "it,test"


