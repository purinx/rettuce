package com.higherkindpud.rettuce.infra.db

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, IO, Resource}
import com.higherkindpud.rettuce.config.MySQLConfig
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

trait MySQLComponents {

  def mySQLConfig: MySQLConfig
  private lazy val executorService: ExecutorService = Executors.newFixedThreadPool(mySQLConfig.threads)
  private lazy val executionContext                 = ExecutionContext.fromExecutorService(executorService)
  private implicit lazy val cs                      = IO.contextShift(executionContext)
  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      // ce <- ExecutionContexts.fixedThreadPool[IO](mySQLConfig.threads) // our connect EC
      be <- Blocker[IO] // our blocking EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        "com.mysql.cj.jdbc.Driver",                                                    // driver classname
        s"jdbc:mysql://${mySQLConfig.host}:${mySQLConfig.port}/${mySQLConfig.dbname}", // connect URL
        mySQLConfig.username,                                                          // username
        mySQLConfig.password,                                                          // password
        executionContext,                                                              // await connection here
        be                                                                             // execute JDBC operations here
      )
    } yield xa
}

object MySQLComponents {
  class DataBaseExecutionContext(underlying: ExecutionContext) extends ExecutionContext {
    override def execute(runnable: Runnable): Unit     = underlying.execute(runnable)
    override def reportFailure(cause: Throwable): Unit = underlying.reportFailure(cause)
  }
}
