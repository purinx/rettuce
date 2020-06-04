package domain.service

import domain.entity.Vegetable
import domain.repository.VegetableRepository

class VegetableService(
    vegetableRepository: VegetableRepository
) {

  def getByName(name: String): Option[Vegetable] = vegetableRepository.getByName(name)

  // TODO sonota no iroiro na domain wo tukatta kijutu wo suru

}
