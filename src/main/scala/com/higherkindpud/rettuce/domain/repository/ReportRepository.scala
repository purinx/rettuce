package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.{Report, Summary}

trait ReportRepository[F[_]] {

  def getByName(name: String): F[Report]

  def settle: F[List[Report]]

  def save(report: Report): F[Unit]

}
