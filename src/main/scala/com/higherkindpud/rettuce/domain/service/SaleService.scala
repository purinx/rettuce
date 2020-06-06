package com.higherkindpud.rettuce.domain.service

import cats.Id
import com.higherkindpud.rettuce.domain.entity.{Report, Summary}
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, SaleRepository, TransactionRunner}
import com.higherkindpud.rettuce.infra.db.DoobieTransactionRunner
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class SaleService[F[_]](
    reportRepository: ReportRepository[Id],
    saleRepository: SaleRepository[F],
    transactionRunner: TransactionRunner[Future],
) {
  import SaleService._
  def settle: Future[SettleResult] = {
    val reports = reportRepository.settle
    for {
      _ <- transactionRunner.run(saleRepository.bulkInsert(reports))
    } yield SettleResult(reports)
  }

}

object SaleService {
  case class SettleResult(sales: List[Report]) {
    def getSummary: Summary = {
      val amount = sales.map(_.amount).sum // sum は部分関数
      val date   = sales.head.date
      Summary(date, amount)
    }
  }
}
