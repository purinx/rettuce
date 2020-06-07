package com.higherkindpud.rettuce.infra.db

import cats.effect.{Blocker, IO, Resource, ContextShift}
import com.higherkindpud.rettuce.config.MySQLConfig
import doobie.hikari.HikariTransactor
import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import doobie.util.ExecutionContexts

trait MySQLComponents {

  def mySQLConfig: MySQLConfig

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
      ec <- ExecutionContexts.fixedThreadPool[IO](mySQLConfig.threads) // our connect EC
      be <- Blocker[IO]                                                // our blocking EC
    } yield {
      implicit val cs: ContextShift[IO] = IO.contextShift(ec)
      HikariTransactor(hiakriDataSource, ec, be)
    }
  }

  lazy val doobieTransactionRunner: ResourceIORunner[ConnectionIO] = new DoobieResourceIORunner(transactor)

}
