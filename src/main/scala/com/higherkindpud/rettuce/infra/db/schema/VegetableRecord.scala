package com.higherkindpud.rettuce.infra.db.schema

import java.util.UUID
import com.higherkindpud.rettuce.domain.entity.{Vegetable, VegetableId}

case class VegetableRecord(id: String, name: String, price: Int) {
  def toEntity: Vegetable = Vegetable(VegetableId(UUID.fromString(id)), name, price)
}

object VegetableRecord {
  def fromEntity(entity: Vegetable) =
    VegetableRecord(
      entity.id.value.toString,
      entity.name,
      entity.price
    )
}
