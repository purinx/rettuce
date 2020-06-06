package com.higherkindpud.rettuce.infra.db

import doobie.implicits._
import cats.effect.{IO, Resource}
import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import doobie.free.ConnectionIO
import doobie.util.transactor.Transactor

import scala.concurrent.Future

class DoobieResourceIORunner(
    transactor: Resource[IO, Transactor[IO]]
) extends ResourceIORunner[ConnectionIO] {

  override def run[A](io: ConnectionIO[A]): Future[A] = {
    transactor.use(xa => io.transact(xa)).unsafeToFuture()
  }

}
