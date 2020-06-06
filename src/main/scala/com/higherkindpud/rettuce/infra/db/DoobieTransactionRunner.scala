package com.higherkindpud.rettuce.infra.db

import cats.effect.IO
import com.higherkindpud.rettuce.domain.repository.TransactionRunner
import doobie.free.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.transactor.Transactor

import scala.concurrent.Future

class DoobieTransactionRunner(
  transactor: Transactor[HikariTransactor[IO]]
) extends TransactionRunner[ConnectionIO] {

  override def run[A](io: ConnectionIO[A]): Future[A] = {
    io.transact(transactor).unsafeRunAsync
  }

}
