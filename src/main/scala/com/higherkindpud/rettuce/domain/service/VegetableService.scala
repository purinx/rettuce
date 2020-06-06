package com.higherkindpud.rettuce.domain.service

import cats.Id
import com.higherkindpud.rettuce.domain.entity.{Report, Vegetable}
import com.higherkindpud.rettuce.domain.repository.{ReportRepository, TransactionRunner, VegetableRepository}

import scala.concurrent.{ExecutionContext, Future}

class VegetableService[F[_], G[_]](
    vegetableRepository: VegetableRepository[F],
    reportRepository: ReportRepository[Id],
    transactionRunner: TransactionRunner[F]
)(implicit defaultExecutionContext: ExecutionContext) {

  def getSaleByName(name: String): Future[Option[Vegetable]] = {
    val a: F[Option[Vegetable]]      = vegetableRepository.getByName(name) // IOっぽいやつ
    val b: Future[Option[Vegetable]] = transactionRunner.run(a)
    b
  }

  def create(vegetable: Vegetable): Unit = vegetableRepository.create(vegetable)

  def incrementQuantity(name: String, quantity: Int): Unit = {
    val report = reportRepository.getByName(name)
    reportRepository.save(Report(name, report.quantity + quantity))
  }
}

object VegetableService {}
