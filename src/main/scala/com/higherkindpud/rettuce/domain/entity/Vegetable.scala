package com.higherkindpud.rettuce.domain.entity

import java.util.UUID

case class VegetableId(value: UUID)

case class Vegetable(id: VegetableId, name: String, price: Int)

object Vegetable {
  def create(name: String, price: Int) = Vegetable(VegetableId(UUID.randomUUID()), name, price)
}
