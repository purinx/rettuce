package infra.redis

import domain.entity.Vegetable
import domain.repository.VegetableRepository

class VegetableRepositoryOnRedis(

) extends VegetableRepository {

  override def getByName(name: String): Option[Vegetable] = ???

}
