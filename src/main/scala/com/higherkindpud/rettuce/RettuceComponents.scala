package com.higherkindpud.rettuce

import com.softwaremill.macwire.wire

import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache

import redis.clients.jedis.JedisPool

import play.api.mvc.ControllerComponents

trait RettuceComponents {

  //domain
  lazy val vegetableService = wire[VegetableService]

  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  //repository
  lazy val pool: JedisPool   = new JedisPool("127.0.0.1", 16379)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}
