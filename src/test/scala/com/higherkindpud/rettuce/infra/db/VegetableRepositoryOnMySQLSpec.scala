package com.higherkindpud.rettuce.infra.db

import pureconfig.generic.auto._
import com.higherkindpud.rettuce.config.RettuceConfig
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.typesafe.config.ConfigFactory
import doobie.free.connection.ConnectionIO
import org.scalatest._
import pureconfig.ConfigSource
import com.higherkindpud.rettuce.domain.entity.Vegetable
import cats._
import cats.implicits._
import scala.concurrent.Future

class VegetableRepositoryOnMySQLSpec extends AsyncFlatSpec {
  lazy val config: RettuceConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[RettuceConfig]
  lazy val mySQLConfig = config.mysql
  lazy val redisConfig = config.redis

  val rdbRunner = new TestDoobieResourceIORunner(mySQLConfig)

  val vegetableRepository: VegetableRepository[ConnectionIO] = new VegetableRepositoryOnMySQL
  import com.higherkindpud.rettuce.domain.repository.VegetableRepository._

  behavior of "vegetableRepositoryOnMySQL"

  "fetchAll" should "全ての野菜が帰ってくる" in {
    val io: ConnectionIO[(List[Long], List[Vegetable])] = for {
      is <- (1 to 10).toList.traverse { i =>
        vegetableRepository.create(CreateVegetable(i.toString, i * 100))
      }
      xs <- vegetableRepository.fetchAll()
    } yield (is, xs)
    rdbRunner.run(io).map {
      case (ids, vegetables) => assert(vegetables.length == 10)
    }
  }

  "getByName" should "存在しない野菜をクエリすると None を返す" in
    rdbRunner.run {
      for {
        o <- vegetableRepository.findByName("carrot")
      } yield (assert(o.isEmpty))
    }

  "create" should "してから getByName することができる" in
    rdbRunner.run {
      for {
        x <- vegetableRepository.create(CreateVegetable("lemon", 80))
        o <- vegetableRepository.findByName("lemon")
      } yield (assert(o.isDefined))
    }
}
