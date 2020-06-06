package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Vegetable

trait VegetableRepository[F[_]] {

  def getByName(name: String): F[Option[Vegetable]]

  def save(vegetable: Vegetable): F[Unit]

}
