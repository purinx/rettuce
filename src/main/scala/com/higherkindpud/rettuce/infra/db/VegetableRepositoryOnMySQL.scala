package com.higherkindpud.rettuce.infra.db

import io.getquill.{idiom => _, _}
import doobie.quill.DoobieContext
import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import doobie.free.connection.ConnectionIO
import doobie.implicits._

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

  def findById(id: Long): ConnectionIO[Option[Vegetable]] =
    run {
      quote {
        querySchema[Vegetable]("vegetables").filter(_.id == lift(id))
      }
    }.map(_.headOption)

  def findByName(name: String): ConnectionIO[Option[Vegetable]] =
    run {
      quote {
        querySchema[Vegetable]("vegetables").filter(_.name == lift(name))
      }
    }.map(_.headOption)

  def create(vegetable: CreateVegetable): ConnectionIO[Long] =
    run {
      quote {
        querySchema[Vegetable]("vegetables").insert(
          _.name  -> lift(vegetable.name),
          _.price -> lift(vegetable.price)
        )
      }
    }
}
