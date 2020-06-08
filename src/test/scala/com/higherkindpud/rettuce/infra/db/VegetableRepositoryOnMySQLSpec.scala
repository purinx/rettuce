package com.higherkindpud.rettuce.infra.db

import java.util.UUID

import pureconfig.generic.auto._
import cats.implicits._
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

  behavior of "vegetableRepositoryOnMySQL"

  "fetchAll" should "全ての野菜が帰ってくる" in {
    val io: ConnectionIO[(List[Long], List[Vegetable])] = for {
      is <- (1 to 10).toList.traverse { i =>
        vegetableRepository.create(Vegetable.create(i.toString, i * 100))
      }
      xs <- vegetableRepository.fetchAll()
    } yield (is, xs)
    rdbRunner.run(io).map {
      case (_, vegetables) => assert(vegetables.length == 10)
    }
  }

  "getByName" should "存在しない野菜をクエリすると None を返す" in {
    val io = vegetableRepository.findByName("carrot")
    rdbRunner.run(io).map { op => assert(op.isEmpty) }
  }

  "create" should "してから getByName することができる" in
    rdbRunner.run {
      for {
        x <- vegetableRepository.create(Vegetable.create("lemon", 80))
        o <- vegetableRepository.findByName("lemon")
      } yield (assert(o.isDefined))
    }
}
