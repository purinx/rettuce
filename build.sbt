import Dependencies._

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
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    name := "playtter",
    commonSettings,
    libraryDependencies += guice,
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= circe,
    libraryDependencies += jedis
  )
