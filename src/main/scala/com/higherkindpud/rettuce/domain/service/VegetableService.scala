package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.repository.TransactionRunner

class VegetableService[F[_]] (
    vegetableRepository: VegetableRepository[F],
    transactionRunner: TransactionRunner[F]
) {

  def getByName(name: String): Future[Option[Vegetable]] = {
    val a: F[Option[Vegetable]] = vegetableRepository.getByName(name) // IOっぽいやつ
    val b: Future[Option[Vegetable]] = transactionRunner.run(a)
    b
  }

  def save(vegetable: Vegetable): Unit = vegetableRepository.save(vegetable)

  def buy(name: String, quantity: Int): Either[] = {
    val vegetable = getByName(name)
    save(Vegetable(name, vegetable.qu))
  }
}

object VegetableService {

}
