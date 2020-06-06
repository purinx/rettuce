package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Vegetable

trait VegetableRepository[F[_]] {
  def getAll: F[List[List[Vegetable]]]
}
