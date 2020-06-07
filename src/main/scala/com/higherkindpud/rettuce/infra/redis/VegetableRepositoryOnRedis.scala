package com.higherkindpud.rettuce.infra.redis

import cats.effect.IO
import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.infra.redis.common.Cache
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._

class VegetableRepositoryOnRedis(
    defaultRedisCache: DefaultRedisCache
) extends VegetableRepository[IO] {

  private implicit val circeDecoder: Decoder[Vegetable] = deriveDecoder
  private implicit val circeEncoder: Encoder[Vegetable] = deriveEncoder
  private val decoder: String => Vegetable =
    parser.parse(_).flatMap(_.as[Vegetable]).getOrElse(throw new RuntimeException("decode error"))
  private val encoder: Vegetable => String = _.asJson.noSpaces

  val vegetableCache: Cache[String, Vegetable] = defaultRedisCache
    .withHash("vegetables")
    .mapValue(decoder, encoder)

  override def getAll(): IO[List[Vegetable]] = ???

  override def getByName(name: String): IO[Option[Vegetable]] = IO(vegetableCache.get(name))

  override def create(vegetable: Vegetable): IO[Unit] = IO(vegetableCache.set(vegetable.name, vegetable))

}
