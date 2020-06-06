package com.higherkindpud.rettuce.infra.redis

import cats.Id

object RedisTransactionRunner extends TransactionRunner[Id] {

  override def run[A](io: A): Future[A] = Future.successful(io)

}
