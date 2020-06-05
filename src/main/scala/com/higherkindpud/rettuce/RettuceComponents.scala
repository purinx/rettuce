package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import com.softwaremill.macwire.wire
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
// import play.api.inject.Module
import com.typesafe.config.ConfigFactory
import play.api.mvc.ControllerComponents
import redis.clients.jedis.JedisPool
import pureconfig.generic.auto._

trait RettuceComponents {
  lazy val config = ConfigFactory.load()
  lazy val appConfig: RettuceConfig = ConfigSource.fromConfig(config).loadOrThrow[RettuceConfig]


  //domain
  lazy val vegetableService = wire[VegetableService]

  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  //repository
  lazy val pool: JedisPool   = new JedisPool(appConfig.redis.host, appConfig.redis.port)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}
