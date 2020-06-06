import Dependencies._

ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / organization := "higherkindpud"

lazy val commonSettings = Seq(
  scalacOptions ++= "-deprecation" :: "-feature" :: "-Xlint" :: Nil,
  scalacOptions in (Compile, console) ~= { _.filterNot(_ == "-Xlint") },
  scalafmtOnCompile := true
)

lazy val playSettings = {
  // [warn] .../rettuce/src/main/resources/routes: Unused import
  // コンパイル時の上記のような警告を消す
  // cf. https://github.com/playframework/playframework/issues/7382
  import play.sbt.routes.RoutesKeys
  Seq(
    RoutesKeys.routesImport := Seq.empty
  )
}

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    name := "rettuce",
    commonSettings,
    playSettings,
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= circe,
    libraryDependencies ++= doobie,
    libraryDependencies ++= macwire,
    libraryDependencies += jedis,
    libraryDependencies += pureconfig,
    libraryDependencies += scalaTest % Test
  )
