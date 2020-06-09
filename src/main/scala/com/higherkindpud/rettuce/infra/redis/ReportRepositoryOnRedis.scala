package com.higherkindpud.rettuce.infra.redis

import cats.effect.IO
import com.higherkindpud.rettuce.domain.entity.Report
import com.higherkindpud.rettuce.domain.repository.ReportRepository
import com.higherkindpud.rettuce.infra.redis.common.{Cache, DefaultRedisCache}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, parser}

class ReportRepositoryOnRedis(
    defaultRedisCache: DefaultRedisCache
) extends ReportRepository[IO] {
  private implicit val circeDecoder: Decoder[Report] = deriveDecoder
  private implicit val circeEncoder: Encoder[Report] = deriveEncoder
  private val decoder: String => Report =
    parser
      .parse(_)
      .flatMap(_.as[Report])
      .getOrElse(throw new RuntimeException("decode error"))
  private val encoder: Report => String = _.asJson.noSpaces
  val reportCache: Cache[String, Report] = defaultRedisCache
    .withHash("reports")
    .mapValue(decoder, encoder)

  def getByName(name: String): IO[Option[Report]] = IO(reportCache.get(name))

  def getAll: IO[List[Report]] = IO(reportCache.getAll()).map(_.toList.map(_._2))

  def save(report: Report): IO[Unit] = IO(reportCache.set(report.name, report))
}
