package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.{ResourceIORunner, VegetableRepository}

import scala.concurrent.Future

trait VegetableService {
  def getAll(): Future[List[Vegetable]]
}

class VegetableServiceWithIO[F[_]](
    vegetableRepository: VegetableRepository[F],
    resourceIORunner: ResourceIORunner[F]
) extends VegetableService {

  def getAll(): Future[List[Vegetable]] = {
    val a: F[List[Vegetable]]      = vegetableRepository.getAll()
    val b: Future[List[Vegetable]] = resourceIORunner.run(a)
    b
  }

}
