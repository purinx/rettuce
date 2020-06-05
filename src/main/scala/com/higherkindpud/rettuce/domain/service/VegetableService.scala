package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository

class VegetableService(
    vegetableRepository: VegetableRepository
) {

  def getByName(name: String): Option[Vegetable] = vegetableRepository.getByName(name)

  def save(vegetable: Vegetable): Unit = vegetableRepository.save(vegetable)

}
