package com.higherkindpud.rettuce.domain.repository

trait TransactionRunner[F[_]] {

  def run[A](io: F[A]): Future[A]

}
