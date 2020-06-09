package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.config.RettuceConfig
import com.typesafe.config.ConfigFactory
import pureconfig.ConfigSource

class ReportRepositoryOnRedisSpec {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val redisConfig = config.redis

}
