package domain.repository

import domain.entity.Vegetable

trait VegetableRepository {

  def getByName(name: String): Option[Vegetable]

}
