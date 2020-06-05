package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.infra.redis.common.{Cache, DefaultRedisCache}
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._

class VegetableRepositoryOnRedis(
    defaultRedisCache: DefaultRedisCache
) extends VegetableRepository {

  private implicit val circeDecoder: Decoder[Vegetable] = deriveDecoder
  private implicit val circeEncoder: Encoder[Vegetable] = deriveEncoder
  private val decoder: String => Vegetable =
    parser.parse(_).flatMap(_.as[Vegetable]).getOrElse(throw new RuntimeException("decode error"))
  private val encoder: Vegetable => String = _.asJson.noSpaces

  val vegetableCache: Cache[String, Vegetable] = defaultRedisCache
    .withHash("vegetables")
    .mapValue(decoder, encoder)

  override def getByName(name: String): Option[Vegetable] = vegetableCache.get(name)

  override def save(vegetable: Vegetable) = vegetableCache.set(vegetable.name, vegetable)
}
