import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.2"
  lazy val jedis     = "redis.clients"  % "jedis"     % "3.3.0"
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
  lazy val doobie = Seq(
    "mysql"         % "mysql-connector-java" % "8.0.17",
    "org.tpolecat" %% "doobie-core"          % "0.8.8",
    "org.tpolecat" %% "doobie-hikari"        % "0.8.8", // HikariCP transactor.
    "org.tpolecat" %% "doobie-quill"         % "0.8.8", // Support for Quill 3.4.10
    "org.tpolecat" %% "doobie-specs2"        % "0.8.8" % "test", // Specs2 support for typechecking statements.
    "org.tpolecat" %% "doobie-scalatest"     % "0.8.8" % "test" // ScalaTest support for typechecking statements.
  )
  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.12.3"
}
