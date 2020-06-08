package com.higherkindpud.rettuce.infra.db

import com.higherkindpud.rettuce.domain.entity.{Vegetable, VegetableId}
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.infra.db.schema.VegetableRecord
import doobie.free.connection.ConnectionIO
import doobie.quill.DoobieContext
import io.getquill.{idiom => _, _}

class VegetableRepositoryOnMySQL extends VegetableRepository[ConnectionIO] {

  val dc = new DoobieContext.MySQL(SnakeCase)
  import dc._

  def fetchAll(): ConnectionIO[List[Vegetable]] =
    run {
      quote {
        querySchema[VegetableRecord]("vegetables")
      }
    }.map(_.map(_.toEntity))

  def findById(id: VegetableId): ConnectionIO[Option[Vegetable]] =
    run {
      quote {
        querySchema[VegetableRecord]("vegetables").filter(_.id == lift(id.value.toString))
      }
    }.map(_.headOption.map(_.toEntity))

  def findByName(name: String): ConnectionIO[Option[Vegetable]] =
    run {
      quote {
        querySchema[VegetableRecord]("vegetables").filter(_.name == lift(name))
      }
    }.map(_.headOption.map(_.toEntity))

  def create(vegetable: Vegetable): ConnectionIO[Long] =
    run {
      quote {
        querySchema[VegetableRecord]("vegetables").insert(
          _.id    -> lift(vegetable.id.value.toString),
          _.name  -> lift(vegetable.name),
          _.price -> lift(vegetable.price)
        )
      }
    }
}
