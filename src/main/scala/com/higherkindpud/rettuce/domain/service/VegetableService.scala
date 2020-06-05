package com.higherkindpud.rettuce.domain.service

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import play.api.Configuration

class VegetableService(
    vegetableRepository: VegetableRepository,
    configuration: Configuration // example di check
) {

  def getByName(name: String): Option[Vegetable] = vegetableRepository.getByName(name)

  def save(vegetable: Vegetable): Unit = vegetableRepository.save(vegetable)

}
