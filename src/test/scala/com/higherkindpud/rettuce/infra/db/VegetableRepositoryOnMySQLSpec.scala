package com.higherkindpud.rettuce.infra.db

import pureconfig.generic.auto._
import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.typesafe.config.ConfigFactory
import doobie.free.connection.ConnectionIO
import org.scalatest._
import pureconfig.ConfigSource

class VegetableRepositoryOnMySQLSpec extends AsyncFlatSpec {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  lazy val redisConfig = config.redis

  val rdbRunner = new TestDoobieResourceIORunner(mySQLConfig)

  val vegetableRepository: VegetableRepository[ConnectionIO] = new VegetableRepositoryOnMySQL
  import com.higherkindpud.rettuce.domain.repository.VegetableRepository._

  behavior of "vegetableRepositoryOnMySQL"

  "fetchAll" should "全ての野菜が帰ってくる" in
    rdbRunner.run {
      for {
        xs <- vegetableRepository.fetchAll()
      } yield (assert(xs.length === 3)) // FIXME 雑
    }

}
