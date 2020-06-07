package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import scala.concurrent.Future
import cats.effect.IO

object RedisResourceIORunner extends ResourceIORunner[IO] {

  override def run[A](io: IO[A]): Future[A] = Future.successful(io.unsafeRunSync())

}
