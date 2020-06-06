package com.higherkindpud.rettuce.infra.db

import cats.effect.{Blocker, IO, Resource}
import com.higherkindpud.rettuce.config.{MySQLConfig}
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

trait MySQLComponents {
  import MySQLComponents._

  def mySQLConfig: MySQLConfig
  def ec: ExecutionContext
  def dataBaseExecutionContext: DataBaseExecutionContext = new DataBaseExecutionContext(ec)
  implicit lazy val cs                                   = IO.contextShift(dataBaseExecutionContext)
  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      be <- Blocker[IO] // our blocking EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        "com.mysql.cj.jdbc.Driver",                                     // driver classname
        s"jdbc:mysql://${mySQLConfig.host}:${mySQLConfig.port}/${mySQLConfig.dbname}", // connect URL
        mySQLConfig.username,                                                // username
        mySQLConfig.password,                                                // password
        ce,                                                             // await connection here
        be                                                              // execute JDBC operations here
      )
    } yield xa
}

object MySQLComponents {
  class DataBaseExecutionContext(underlying: ExecutionContext) extends ExecutionContext {
    override def execute(runnable: Runnable): Unit     = underlying.execute(runnable)
    override def reportFailure(cause: Throwable): Unit = underlying.reportFailure(cause)
  }
}
