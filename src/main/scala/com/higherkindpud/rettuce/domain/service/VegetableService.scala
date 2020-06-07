package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.{Report, Vegetable}
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, ResourceIORunner, VegetableRepository}

import scala.concurrent.{ExecutionContext, Future}

trait VegetableService {
  def getSaleByName(name: String): Future[Option[Vegetable]]
  def create(vegetable: Vegetable): Future[Unit]
  def incrementQuantity(name: String, quantity: Int): Future[Unit]
}

class VegetableServiceWithIO[F[_], G[_]](
    vegetableRepository: VegetableRepository[F],
    rdbRunner: ResourceIORunner[F],
    reportRepository: ReportRepository[G],
    kvsRunner: ResourceIORunner[G]
)(implicit defaultExecutionContext: ExecutionContext)
    extends VegetableService {

  def getSaleByName(name: String): Future[Option[Vegetable]] = {
    rdbRunner.run { vegetableRepository.getByName(name) }
  }

  def create(vegetable: Vegetable): Future[Unit] = rdbRunner.run(vegetableRepository.create(vegetable))

  def incrementQuantity(name: String, quantity: Int): Future[Unit] = {
    val reportOptAsnyc: Future[Option[Report]] = kvsRunner.run(reportRepository.getByName(name))
    reportOptAsnyc.map { reportOpt =>
      reportOpt.foreach { report =>
        reportRepository.save(Report(name, report.quantity + quantity))
      }
    }
  }
}
