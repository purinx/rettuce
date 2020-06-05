package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import com.softwaremill.macwire.wire
import domain.service.VegetableService
import play.api.{Configuration, Environment}
import play.api.mvc.{ControllerComponents, DefaultControllerComponents}
import play.api.inject.{Binding, Module}
import redis.clients.jedis.JedisPool

class AppModule extends Module {
  //controller
  lazy val controllerComponents: ControllerComponents = wire[DefaultControllerComponents]

  //domain
  lazy val pool: JedisPool   = new JedisPool("127.0.0.1", 16379)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableService                         = wire[VegetableService]
  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    
  }
}
