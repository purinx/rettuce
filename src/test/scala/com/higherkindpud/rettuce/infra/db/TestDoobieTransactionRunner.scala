package com.higherkindpud.rettuce.infra.db

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.higherkindpud.rettuce.config.MySQLConfig
import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.free.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

import scala.concurrent.Future

class TestDoobieResourceIORunner() extends ResourceIORunner[ConnectionIO] {
  // ここで resource を読み込みたい
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
  override def run[A](io: ConnectionIO[A]): Future[A] = {
    transactor.use(xa => {
      io.transact(xa)
      Transactor.after.set(xa, HC.roolback)
    })
  }

}
