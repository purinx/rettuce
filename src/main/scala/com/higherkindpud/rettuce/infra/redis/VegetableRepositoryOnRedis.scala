package infra.redis

import domain.entity.Vegetable
import domain.repository.VegetableRepository
import infra.redis.common.{Cache, DefaultRedisCache}
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

}
