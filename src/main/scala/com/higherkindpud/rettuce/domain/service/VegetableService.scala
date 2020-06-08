package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.{Report, Vegetable}
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, ResourceIORunner, VegetableRepository}
import com.higherkindpud.rettuce.domain.service.VegetableService.CreateVegetable

import scala.concurrent.{ExecutionContext, Future}

object VegetableService {
  case class CreateVegetable(name: String, price: Int)
}

trait VegetableService {
  import VegetableService._

  def getSaleByName(name: String): Future[Option[Vegetable]]
  def create(vegetable: CreateVegetable): Future[Long]
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
    rdbRunner.run { vegetableRepository.findByName(name) }
  }

  def create(vegetable: CreateVegetable): Future[Long] =
    rdbRunner.run {
      vegetableRepository.create {
        VegetableRepository.CreateVegetable(vegetable.name, vegetable.price)
      }
    }

  def incrementQuantity(name: String, quantity: Int): Future[Unit] = {
    val reportOptAsnyc: Future[Option[Report]] = kvsRunner.run(reportRepository.getByName(name))
    reportOptAsnyc.map { reportOpt =>
      reportOpt.foreach { report =>
        reportRepository.save(Report(name, report.quantity + quantity))
      }
    }
  }
}
