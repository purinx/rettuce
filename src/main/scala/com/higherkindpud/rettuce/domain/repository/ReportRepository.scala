package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Report

trait ReportRepository[F[_]] {

  def getByName(name: String): F[Option[Report]]

  def getAll: F[List[Report]]

  def save(report: Report): F[Unit]

}
