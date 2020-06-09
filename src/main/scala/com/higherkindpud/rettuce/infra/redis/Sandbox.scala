package com.higherkindpud.rettuce.infra.redis

import com.higherkindpud.rettuce.infra.redis.free.Nyan
import com.higherkindpud.rettuce.infra.redis.free.Nyan._

object Sandbox {

  val a: RedisIO[Int] = Nyan.pure(42)

}
