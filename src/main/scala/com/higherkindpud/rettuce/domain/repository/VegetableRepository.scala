package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Vegetable

trait VegetableRepository[F[_]] {
  def getAll: F[List[List[Vegetable]]]

  def getByName(name: String): F[Option[Vegetable]]

  def create(vegetable: Vegetable): F[Unit]

}
