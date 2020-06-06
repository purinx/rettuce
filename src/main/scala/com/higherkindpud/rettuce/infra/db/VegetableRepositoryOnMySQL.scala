package com.higherkindpud.rettuce.infra.db

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository

import doobie.free.ConnectionIO

class VegetableRepositoryOnMySQL extends VegetableRepository[ConnectionIO] {
  def getByName(name: String): ConnectionIO[Option[Vegetable]]
  def save(vegetable: Vegetable): ConnectionIO[Unit]
}
