package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.db.MySQLComponents
import com.higherkindpud.rettuce.infra.redis.RedisComponents
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.softwaremill.macwire.wire
import com.typesafe.config.ConfigFactory
import play.api.mvc.ControllerComponents
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import cats.Id
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import doobie.free.connection.ConnectionIO

trait RettuceComponents extends MySQLComponents with RedisComponents {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  lazy val redisConfig = config.redis

  //domain
  lazy val vegetableService: VegetableService[Any] = wire[VegetableService[Id]]
  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  // repository
  lazy val vegetableRepository: VegetableRepository[Id] = wire[VegetableRepositoryOnRedis]
  // lazy val vegetableRepository: VegetableRepository[ConnectionIO] = ??? // これを渡すとコンパイルエラーになるのでヨシ
}
