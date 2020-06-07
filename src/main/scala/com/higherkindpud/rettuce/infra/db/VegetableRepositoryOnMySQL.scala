package com.higherkindpud.rettuce.infra.db

import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import doobie.free.connection.ConnectionIO

class VegetableRepositoryOnMySQL extends VegetableRepository[ConnectionIO] {
  def fetchAll(): ConnectionIO[List[Vegetable]]                = ???
  def getByName(name: String): ConnectionIO[Option[Vegetable]] = ???
  def create(vegetable: Vegetable): ConnectionIO[Unit]         = ???
}
