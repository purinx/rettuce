package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Sale

trait SaleRepository[F[_]] {

  def findByName(name: String): F[Option[Sale]]

  def fetchAll: F[List[Sale]]

  def save(sale: Sale): F[Unit]
}

object SaleRepository {
  case class SaveRequest(vegetableName: Int, quantity: Int, amount: Int)
}
