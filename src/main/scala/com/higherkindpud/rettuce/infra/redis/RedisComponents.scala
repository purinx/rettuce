package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import cats.Id

import com.softwaremill.macwire.wire
import redis.clients.jedis.JedisPool

import com.higherkindpud.rettuce.config.RedisConfig

trait RedisComponents {

  def redisConfig: RedisConfig

  //repository
  lazy val jedisPool: JedisPool = new JedisPool(redisConfig.host, redisConfig.port)
  lazy val defaultRedisCache    = wire[DefaultRedisCache]

  lazy val redisResourceIORunner: ResourceIORunner[Id] = RedisResourceIORunner

}
