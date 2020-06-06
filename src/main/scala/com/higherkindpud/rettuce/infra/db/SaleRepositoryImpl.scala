package com.higherkindpud.rettuce.infra.db

import com.higherkindpud.rettuce.domain.entity.Sale
import com.higherkindpud.rettuce.domain.repository.SaleRepository

class SaleRepositoryImpl extends SaleRepository {

  override def settle: List[Sale] = {
    val vegetables = ???
  }
}
