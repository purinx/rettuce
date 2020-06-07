package com.higherkindpud.rettuce.domain.repository

import cats.Monad

object IOTypes {

  type RDB[F[_]] = Monad[F]
  type KVS[F[_]] = F[_]

}
