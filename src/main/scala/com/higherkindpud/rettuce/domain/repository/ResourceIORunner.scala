package com.higherkindpud.rettuce.domain.repository

import scala.concurrent.Future

trait ResourceIORunner[F[_]] {

  def run[A](io: F[A]): Future[A]

}
