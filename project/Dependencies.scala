import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.2"
  lazy val redis = "redis.clients" % "jedis" % "3.3.0"
  lazy val circe = Seq(
    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0"
  )
}
