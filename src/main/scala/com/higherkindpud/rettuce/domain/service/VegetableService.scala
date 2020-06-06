package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, TransactionRunner, VegetableRepository}

import scala.concurrent.Future

class VegetableService[F[_], G[_]] (
                               vegetableRepository: VegetableRepository[F],
                               reportRepository: ReportRepository[G],
                               transactionRunner: TransactionRunner[F]
) {

  def getSaleByName(name: String): Future[Option[Vegetable]] = {
    val a: F[Option[Vegetable]] = vegetableRepository.getByName(name) // IOっぽいやつ
    val b: Future[Option[Vegetable]] = transactionRunner.run(a)
    b
  }

  def save(vegetable: Vegetable): Unit = vegetableRepository.save(vegetable)

  def buy(name: String, quantity: Int): Either[] = {
    val sale = getByName(name)
    save(Vegetable(name, sale.quantiry))
  }
}

object VegetableService {

}
