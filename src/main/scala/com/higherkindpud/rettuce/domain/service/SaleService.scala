package com.higherkindpud.rettuce.domain.service

import java.time.Instant

import cats.Id
import com.higherkindpud.rettuce.domain.entity.{Report, Sale, Summary}
import com.higherkindpud.rettuce.domain.repository.{
  ReportRepository,
  SaleRepository,
  ResourceIORunner,
  VegetableRepository
}

import scala.concurrent.{ExecutionContext, Future}

trait SaleService {
  import SaleService._
  def settle: Future[SettleResult]
}

object SaleService {
  case class SettleResult(sales: List[Sale]) {
    def getSummary: Summary = {
      // val amount = sales.map(_.amount).sum // sum は部分関数
      // val date   = sales.head.date
      // Summary(date, amount)
      ???
    }
  }
}

class SaleServiceWithIO[F[_]](
    reportRepository: ReportRepository[Id],
    saleRepository: SaleRepository[F],
    resourceIORunner: ResourceIORunner[F],
    vegetableRepository: VegetableRepository[F]
)(implicit defaultExecutionContext: ExecutionContext)
    extends SaleService {
  import SaleService._

  def settle: Future[SettleResult] = {
    val date = Instant.now()
    val salesF: Future[List[Sale]] = for {
      vegetables <- resourceIORunner.run(vegetableRepository.getAll)
    } yield vegetables.map(v => {
      reportRepository.getByName(v.name) match {
        case None         => Sale(v.id, 0, 0, date)
        case Some(report) => Sale(v.id, report.quantity, report.quantity * v.price, date)
      }
    })
    salesF.map { sales =>
      resourceIORunner.run(saleRepository.bulkInsert(sales))
      SettleResult(sales)
    }
  }
}
