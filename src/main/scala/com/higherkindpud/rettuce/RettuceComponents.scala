package com.higherkindpud.rettuce

// 消さないこと
import pureconfig.generic.auto._
import java.time.Clock

import cats.effect.IO
import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, SaleRepository, VegetableRepository}
import com.higherkindpud.rettuce.domain.service.{
  SaleService,
  SaleServiceWithIO,
  VegetableService,
  VegetableServiceWithIO
}
import com.higherkindpud.rettuce.infra.db.{MySQLComponents, SaleRepositoryOnMySQL, VegetableRepositoryOnMySQL}
import com.higherkindpud.rettuce.infra.redis.{RedisComponents, ReportRepositoryOnRedis}
import com.softwaremill.macwire.wire
import com.typesafe.config.ConfigFactory
import doobie.free.connection.ConnectionIO
import play.api.mvc.ControllerComponents
import pureconfig.ConfigSource

import scala.concurrent.ExecutionContext

trait RettuceComponents extends MySQLComponents with RedisComponents {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  lazy val redisConfig = config.redis

  //domain
  lazy val clock: Clock = Clock.systemUTC()
  implicit def executionContext: ExecutionContext
  lazy val vegetableService: VegetableService = wire[VegetableServiceWithIO[ConnectionIO, IO]]
  lazy val saleService: SaleService           = wire[SaleServiceWithIO[ConnectionIO, IO]]
  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  // repository
  lazy val vegetableRepository: VegetableRepository[ConnectionIO] = wire[VegetableRepositoryOnMySQL]
  lazy val reportRepository: ReportRepository[IO]                 = wire[ReportRepositoryOnRedis]
  lazy val saleRepository: SaleRepository[ConnectionIO]           = wire[SaleRepositoryOnMySQL]
}
