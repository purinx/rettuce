import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.2"
  lazy val jedis     = "redis.clients" % "jedis"      % "3.3.0"
  lazy val circe = Seq(
    "io.circe" %% "circe-core"    % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser"  % "0.13.0"
  )
  lazy val macwire = Seq(
    "com.softwaremill.macwire" %% "macros"     % "2.3.6" % "provided",
    "com.softwaremill.macwire" %% "macrosakka" % "2.3.6" % "provided",
    "com.softwaremill.macwire" %% "util"       % "2.3.6",
    "com.softwaremill.macwire" %% "proxy"      % "2.3.6"
  )
  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.12.3"
}
