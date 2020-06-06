package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.domain.repository.ResourceIORunner
import scala.concurrent.Future
import cats.Id

object RedisResourceIORunner extends ResourceIORunner[Id] {

  override def run[A](io: A): Future[A] = Future.successful(io)

}
