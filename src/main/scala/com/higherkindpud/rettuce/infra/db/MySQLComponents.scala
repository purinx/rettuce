package com.higherkindpud.rettuce.infra.db

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, IO, Resource}
import com.higherkindpud.rettuce.config.MySQLConfig
import doobie.hikari.HikariTransactor
import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import doobie.util.ExecutionContexts

import scala.concurrent.ExecutionContext

trait MySQLComponents {

  def mySQLConfig: MySQLConfig

  private lazy val executorService: ExecutorService = Executors.newFixedThreadPool(mySQLConfig.threads)
  private lazy val executionContext                 = ExecutionContext.fromExecutorService(executorService)
  private implicit lazy val cs                      = IO.contextShift(executionContext)
  lazy val transactor: Resource[IO, Transactor[IO]] = {
    lazy val hiakriDataSource: HikariDataSource = {
      val config = new HikariConfig()
      config.setDriverClassName("com.mysql.cj.jdbc.Driver")
      config.setJdbcUrl(s"jdbc:mysql://${mySQLConfig.host}:${mySQLConfig.port}/${mySQLConfig.dbname}")
      config.setUsername(mySQLConfig.username)
      config.setPassword(mySQLConfig.password)
      new HikariDataSource(config)
    }
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](mySQLConfig.threads) // our connect EC
      be <- Blocker[IO]                                                // our blocking EC
    } yield HikariTransactor(hiakriDataSource, ce, be)
  }

  lazy val doobieTransactionRunner: ResourceIORunner[ConnectionIO] = new DoobieResourceIORunner(transactor)

}

object MySQLComponents {
  class DataBaseExecutionContext(underlying: ExecutionContext) extends ExecutionContext {
    override def execute(runnable: Runnable): Unit     = underlying.execute(runnable)
    override def reportFailure(cause: Throwable): Unit = underlying.reportFailure(cause)
  }
}
