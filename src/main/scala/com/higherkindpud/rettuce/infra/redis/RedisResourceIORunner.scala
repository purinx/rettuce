package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import scala.concurrent.Future
import cats.effect.IO

object RedisResourceIORunner extends ResourceIORunner[RedisIO] {

  override def run[A](io: RedisIO[A]): Future[A] = Future.successful(io.unsafeRunSync())

}
