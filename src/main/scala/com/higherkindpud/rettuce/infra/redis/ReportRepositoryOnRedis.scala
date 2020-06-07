package com.higherkindpud.rettuce.infra.redis

import cats.effect.IO
import com.higherkindpud.rettuce.domain.entity.Report
import com.higherkindpud.rettuce.domain.repository.ReportRepository

class ReportRepositoryOnRedis extends ReportRepository[IO] {
  def getByName(name: String): IO[Option[Report]] = ???
  def getAll: IO[List[Report]]                    = ???
  def save(report: Report): IO[Unit]              = ???
}
