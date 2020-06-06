package com.higherkindpud.rettuce.infra.db

import doobie.free.ConnectionIO

class DoobieTransactionRunner(
  transactor: Resource[IO, HikariTransactor[IO]]
) extends TransactionRunner[ConnectionIO] {

  override def run[A](io: ConnectionIO[A]): Future[A] = {
    io.transact(transactor).unsafeRunAsync
  }

}
