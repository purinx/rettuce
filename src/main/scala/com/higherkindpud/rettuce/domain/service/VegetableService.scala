package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.{ResourceIORunner, VegetableRepository}

import scala.concurrent.Future

trait VegetableService {

  type F[_]
  def vegetableRepository: VegetableRepository[F]
  def resourceIORunner: ResourceIORunner[F]

  def getAll(): Future[List[Vegetable]] = {
    val a: F[List[Vegetable]]      = vegetableRepository.getAll()
    val b: Future[List[Vegetable]] = resourceIORunner.run(a)
    b
  }

}
