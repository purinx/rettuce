name := """playtter"""
organization := "higherkindpud"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.dripower" %% "play-circe" % "2712.0"
libraryDependencies += "redis.clients" % "jedis" % "3.3.0"
