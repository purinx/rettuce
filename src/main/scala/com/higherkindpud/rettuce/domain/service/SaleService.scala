package com.higherkindpud.rettuce.domain.service

import java.time.Instant

import cats.Id
import com.higherkindpud.rettuce.domain.entity.{Report, Sale, Summary}
import com.higherkindpud.rettuce.domain.repository.{
  ReportRepository,
  SaleRepository,
  TransactionRunner,
  VegetableRepository
}

import scala.concurrent.{ExecutionContext, Future}

class SaleService[F[_]](
    reportRepository: ReportRepository[Id],
    saleRepository: SaleRepository[F],
    transactionRunner: TransactionRunner[F],
    vegetableRepository: VegetableRepository[F]
)(implicit defaultExecutionContext: ExecutionContext) {
  import SaleService._

  def settle: Future[SettleResult] = {
    val date = Instant.now()
    val salesF = for {
      vegetables <- transactionRunner.run(vegetableRepository.getAll)
    } yield vegetables.map(v => {
      reportRepository.getByName(v.name) match {
        case None         => Sale(v.id, 0, 0, date)
        case Some(report) => Sale(v.id, report.quantity, report.quantity * v.price, date)
      }
    })
    salesF.map { sales =>
      transactionRunner.run(saleRepository.bulkInsert(sales))
      SettleResult(sales)
    }
  }
}

object SaleService {
  case class SettleResult(sales: List[Sale]) {
    def getSummary: Summary = {
      val amount = sales.map(_.amount).sum // sum は部分関数
      val date   = sales.head.date
      Summary(date, amount)
    }
  }
}
