package com.higherkindpud.rettuce.domain.service

import java.time.Instant

import com.higherkindpud.rettuce.domain.entity.{Report, Sale, Summary}
import com.higherkindpud.rettuce.domain.repository.{
  ReportRepository,
  ResourceIORunner,
  SaleRepository,
  VegetableRepository
}

import scala.concurrent.{ExecutionContext, Future}
import java.time.Clock

import cats.Monad

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

  // from, to: EpochMilli
  case class DateSpan(from: Long, to: Long) {
    def fromAsInstant: Instant = Instant.ofEpochMilli(from)
    def toAsInstant: Instant   = Instant.ofEpochMilli(to)
  }
}

/**
  * F[_]: RDB, G[_]: KVS
  */
class SaleServiceWithIO[F[_]: Monad, G[_]: Monad](
    saleRepository: SaleRepository[F],
    rdbRunner: ResourceIORunner[F],
    vegetableRepository: VegetableRepository[F],
    reportRepository: ReportRepository[G],
    kvsRunner: ResourceIORunner[G],
    clock: Clock
)(implicit defaultExecutionContext: ExecutionContext)
    extends SaleService {
  import SaleService._

  def getReportPadded(name: String): Future[Report] =
    kvsRunner.run {
      Monad[G].map(reportRepository.getByName(name))(_.getOrElse(Report(name, 0)))
    }
  def settle: Future[SettleResult] = {
    val date: Instant = clock.instant()
    for {
      vegetables <- rdbRunner.run(vegetableRepository.getAll)
      reports    <- Future.sequence { vegetables.map(v => getReportPadded(v.name)) }
      sales = (vegetables zip reports).map {
        case (v, r) => Sale(v.id, r.quantity, r.quantity * v.price, date)
      }
      _ <- rdbRunner.run(saleRepository.bulkInsert(sales))
    } yield SettleResult(sales)
  }
}
