package com.higherkindpud.rettuce.infra.db

import java.time.Instant

import com.higherkindpud.rettuce.domain.repository.SaleRepository
import com.higherkindpud.rettuce.domain.entity.Sale
import doobie.free.ConnectionIO

class SaleRepositoryOnMySQL extends SaleRepository[ConnectionIO] {

  def fetchAll(): ConnectionIO[List[Sale]] = ???
  def findByName(name: String): ConnectionIO[Option[Sale]] = ???
  def save(sale: Sale): ConnectionIO[Unit] = ???
}

object SaleRepository {
  case class SaveSaleRequest(vegetableId: Int, quantity: Int, amount: Int, date: Instant)
}
