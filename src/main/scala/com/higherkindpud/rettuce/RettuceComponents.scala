package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.service.{VegetableService, VegetableServiceWithIO}
import com.higherkindpud.rettuce.infra.db.MySQLComponents
import com.higherkindpud.rettuce.infra.redis.RedisComponents
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.softwaremill.macwire.wire
import com.typesafe.config.ConfigFactory
import play.api.mvc.ControllerComponents
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import cats.effect.IO
import com.higherkindpud.rettuce.domain.repository.{VegetableRepository, ReportRepository, SaleRepository}
import scala.concurrent.ExecutionContext
import com.higherkindpud.rettuce.domain.service.{SaleService, SaleServiceWithIO}
import doobie.free.connection.ConnectionIO
import com.higherkindpud.rettuce.infra.db.SaleRepositoryOnMySQL
import java.time.Clock

trait RettuceComponents extends MySQLComponents with RedisComponents {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  lazy val redisConfig = config.redis

  //domain
  lazy val clock: Clock = Clock.systemUTC()
  implicit def executionContext: ExecutionContext
  lazy val vegetableService: VegetableService = wire[VegetableServiceWithIO[IO]]
  lazy val saleService: SaleService           = wire[SaleServiceWithIO[ConnectionIO, IO]]
  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  // repository
  lazy val vegetableRepository: VegetableRepository[IO] = wire[VegetableRepositoryOnRedis]
  lazy val reportRepository: ReportRepository[IO]       = ??? // FIXME
  lazy val saleRepository: SaleRepository[ConnectionIO] = wire[SaleRepositoryOnMySQL]
}
