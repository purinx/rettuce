ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version      := "1.0-SNAPSHOT"
ThisBuild / organization := "higherkindpud"

lazy val commonSettings = Seq(
  scalacOptions ++= "-deprecation" :: "-feature" :: "-Xlint" :: Nil,
  scalacOptions in (Compile, console) ~= {_.filterNot(_ == "-Xlint")},
  scalafmtOnCompile := true
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "playtter",
    commonSettings,
    libraryDependencies += guice,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
    libraryDependencies += "com.dripower" %% "play-circe" % "2712.0",
    libraryDependencies += "redis.clients" % "jedis" % "3.3.0"
  )
