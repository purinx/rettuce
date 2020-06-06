package com.higherkindpud.rettuce.infra.db

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository

class VegetableRepositoryOnMySQL extends VegetableRepository {
  def getByName(name: String): Option[Vegetable]

}
