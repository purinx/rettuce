package com.higherkindpud.rettuce.infra.db

import io.getquill.{idiom => _, _}
import doobie.quill.DoobieContext
import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import doobie.free.connection.ConnectionIO

class VegetableRepositoryOnMySQL extends VegetableRepository[ConnectionIO] {
  import VegetableRepository._

  val dc = new DoobieContext.MySQL(SnakeCase)
  import dc._

  def fetchAll(): ConnectionIO[List[Vegetable]] =
    run {
      quote {
        querySchema[Vegetable]("vegetables")
      }
    }
  def getByName(name: String): ConnectionIO[Option[Vegetable]] = ???
  def create(vegetable: CreateVegetable): ConnectionIO[Unit]   = ???
}
