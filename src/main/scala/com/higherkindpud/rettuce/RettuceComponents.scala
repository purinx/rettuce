package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import com.softwaremill.macwire.wire
import play.api.inject.Module
import redis.clients.jedis.JedisPool

trait RettuceComponents extends Module {
  //controller
  //lazy val controllerComponents: ControllerComponents = wire[DefaultControllerComponents]


  //domain
  lazy val pool: JedisPool   = new JedisPool("127.0.0.1", 16379)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableService                         = wire[VegetableService]


  //repository
  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}
