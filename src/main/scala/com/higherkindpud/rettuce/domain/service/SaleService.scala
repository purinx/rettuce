package com.higherkindpud.rettuce.domain.service

import java.time.Instant

import com.higherkindpud.rettuce.domain.entity.{Sale, Summary}
import com.higherkindpud.rettuce.domain.repository.{
  ReportRepository,
  SaleRepository,
  ResourceIORunner,
  VegetableRepository
}
import com.higherkindpud.rettuce.domain.repository.IOTypes.{RDB, KVS}

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

class SaleServiceWithIO[F[_]: RDB, G[_]: KVS](
    saleRepository: SaleRepository[F],
    vegetableRepository: VegetableRepository[F],
    rdbRunner: ResourceIORunner[F],
    reportRepository: ReportRepository[G],
    kvsRunner: ResourceIORunner[G]
)(implicit defaultExecutionContext: ExecutionContext)
    extends SaleService {
  import SaleService._

  def settle: Future[SettleResult] = {
    val date = Instant.now()
    for {
      vegetables <- rdbRunner.run(vegetableRepository.getAll)
      reportOpts <- Future.sequence {
        vegetables.map(v => kvsRunner.run(reportRepository.getByName(v.name)))
      }
      sales = (vegetables zip reportOpts).map {
        case (v, r) =>
          Sale(v.id, r.map(_.quantity).getOrElse(0), r.map(_.quantity * v.price).getOrElse(0), date)
      }
      _ <- rdbRunner.run(saleRepository.bulkInsert(sales))
    } yield SettleResult(sales)
  }
}
