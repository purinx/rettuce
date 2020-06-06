package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.{Sale, Summary}

trait SaleRepository {

  def settle: List[Sale]

}
