package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import com.softwaremill.macwire.wire
import play.api.inject.Module
import play.api.mvc._
import redis.clients.jedis.JedisPool

trait RettuceComponents extends Module with SystemComponents {

  //domain
  lazy val pool: JedisPool   = new JedisPool("127.0.0.1", 16379)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableService = wire[VegetableService]

  //controller
  lazy val vegetableController: BaseController = wire[VegetableController]

  //repository
  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}
