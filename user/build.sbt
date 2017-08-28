import java.util.UUID

name := "user"
organization := "com.notnamed.user"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")



enablePlugins(DockerComposePlugin)
lazy val docker = taskKey[Unit]("IntegrationTests with Running Docker Container") in IntegrationTest
docker := {
  val projectId = UUID.randomUUID().toString
  s"docker-compose -p $projectId up -d" !;
  s"docker wait ${s"docker-compose -p $projectId ps -q it-tests"!!}" !;
  s"docker logs ${s"docker-compose -p $projectId ps -q it-tests"!!}" !;
  s"docker-compose -p $projectId stop" !;
}

//  .configs(DockerIntegrationTest)
//  .settings(Defaults.itSettings)
//  .enablePlugins(DockerComposePlugin)


lazy val commons = RootProject(file("../commons"))
val user =
  project.in(file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings)
    .aggregate(commons)
    .dependsOn(commons)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "it,test"
libraryDependencies += "org.mockito" % "mockito-all" % "2.+" % "it,test"


