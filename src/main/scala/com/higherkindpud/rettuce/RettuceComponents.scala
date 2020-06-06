package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.db.MySQLComponents
import com.higherkindpud.rettuce.infra.redis.RedisComponents
import com.softwaremill.macwire.wire
import com.typesafe.config.ConfigFactory
import play.api.mvc.ControllerComponents
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import redis.clients.jedis.JedisPool

trait RettuceComponents
  extends MySQLComponents
  with RedisComponents {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  //domain
  lazy val vegetableService = wire[VegetableService]

  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}
