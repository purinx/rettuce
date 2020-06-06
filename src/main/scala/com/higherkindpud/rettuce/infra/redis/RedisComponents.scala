package com.higherkindpud.rettuce.infra.redis

package com.higherkindpud.rettuce.domain.repository.TransactionRunner
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import cats.Id

trait RedisComponets {

  //repository
  lazy val jedisPool: JedisPool   = new JedisPool(config.redis.host, config.redis.port)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val redisTransactionRunner: TransactionRunner[Id] = RedisTransactionRunner

}
