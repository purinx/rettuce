package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.{Report, Vegetable}
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, ResourceIORunner, VegetableRepository}

import scala.concurrent.{ExecutionContext, Future}

trait VegetableService {
  def getSaleByName(name: String): Future[Option[Vegetable]]
  def create(vegetable: Vegetable): Future[Unit]
  def incrementQuantity(name: String, quantity: Int): Future[Unit]
}

class VegetableServiceWithIO[F[_]](
    vegetableRepository: VegetableRepository[F],
    reportRepository: ReportRepository[F],
    runner: ResourceIORunner[F]
)(implicit defaultExecutionContext: ExecutionContext)
    extends VegetableService {

  def getSaleByName(name: String): Future[Option[Vegetable]] = {
    val a: F[Option[Vegetable]]      = vegetableRepository.getByName(name) // IOっぽいやつ
    val b: Future[Option[Vegetable]] = runner.run(a)
    b
  }

  def create(vegetable: Vegetable): Future[Unit] = runner.run(vegetableRepository.create(vegetable))

  def incrementQuantity(name: String, quantity: Int): Future[Unit] = {
    val reportOptAsnyc: Future[Option[Report]] = runner.run(reportRepository.getByName(name))
    reportOptAsnyc.map { reportOpt =>
      reportOpt.foreach { report =>
        reportRepository.save(Report(name, report.quantity + quantity))
      }
    }
  }
}

object VegetableService {}
