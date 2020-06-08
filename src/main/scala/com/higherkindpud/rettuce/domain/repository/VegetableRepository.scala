package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.{Vegetable, VegetableId}

trait VegetableRepository[F[_]] {

  def fetchAll(): F[List[Vegetable]]

  def findById(id: VegetableId): F[Option[Vegetable]]

  def findByName(name: String): F[Option[Vegetable]]

  def create(vegetable: Vegetable): F[Long]

}
