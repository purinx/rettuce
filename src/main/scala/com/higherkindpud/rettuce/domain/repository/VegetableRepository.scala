package com.higherkindpud.rettuce.domain.repository

import com.higherkindpud.rettuce.domain.entity.Vegetable

trait VegetableRepository {

  def getByName(name: String): Option[Vegetable]

}
