package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Vegetable

object VegetableRepository {
  case class CreateVegetable(name: String, price: Int)
}

trait VegetableRepository[F[_]] {
  import VegetableRepository._

  def fetchAll(): F[List[Vegetable]]

  def findById(id: Long): F[Option[Vegetable]]

  def findByName(name: String): F[Option[Vegetable]]

  def create(vegetable: CreateVegetable): F[Long]

}
