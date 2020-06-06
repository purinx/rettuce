package com.higherkindpud.rettuce.domain.repository

import scala.concurrent.Future

trait TransactionRunner[F[_]] {

  def run[A](io: F[A]): Future[A]

}
