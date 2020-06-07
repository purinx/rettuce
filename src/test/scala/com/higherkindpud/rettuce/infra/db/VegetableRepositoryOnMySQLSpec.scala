package com.higherkindpud.rettuce.infra.db

import pureconfig.generic.auto._
import com.higherkindpud.rettuce.config.RedisConfig
import com.typesafe.config.{Config, ConfigFactory}
import doobie.specs2.analysisspec.IOChecker
import org.scalatest.FlatSpec
import pureconfig.ConfigSource

class VegetableRepositoryOnMySQLSpec extends FlatSpec with IOChecker {
  val conf: Config = ConfigFactory.load()
  val conf_redis: Config = conf.getConfig("redis")
  val redisconfig: RedisConfig = ConfigSource.fromConfig(conf_redis).loadOrThrow[RedisConfig]

}
