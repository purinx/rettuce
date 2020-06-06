package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.{Report, Summary}
import com.higherkindpud.rettuce.domain.repository.ReportRepository

class SaleService(
    saleRepository: ReportRepository
) {
  import SaleService._
  def settle: SettleResult = SettleResult(saleRepository.settle)
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
