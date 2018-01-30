name := "groups"
organization := "com.notnamed.groups"
version := "1.0"
scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
mainClass in (Compile,run) := Some("com.notnamed.groups.Groups")

lazy val commons = RootProject(file("../commons"))
val circeVersion = "0.9.0"
libraryDependencies ++= {
  Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )
}
val groups = project.in(file("."))
    .aggregate(commons)
    .dependsOn(commons)

