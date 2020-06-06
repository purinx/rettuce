package com.higherkindpud.rettuce.controller

import javax.inject._
import play.api.mvc._
import akka.util.ByteString
import io.circe.{Decoder, Json, parser}
import com.higherkindpud.rettuce.controller.util.CirceWritable._
import com.higherkindpud.rettuce.domain.entity.Vegetable
import com.higherkindpud.rettuce.domain.service.VegetableService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VegetableController(
    controllerComponents: ControllerComponents,
    vegetableService: VegetableService
) extends AbstractController(controllerComponents) {

  import VegetableController._

  def apple() =
    Action { implicit request: Request[AnyContent] =>
      val json: Json = vegetableEncoder(Vegetable("apple", 500, 44))
      Ok(json)
    }

  def banana() =
    Action(parse.byteString) { implicit request: Request[ByteString] =>
      val str: String = request.body.utf8String // POSTのボディ
      // これがあればあとはDecoderを定義すればなんとでもJsonを扱える

      val response   = Response(str)
      val json: Json = responseEncoder(response)

      Ok(json)
    }

  def save() =
    Action(parse.byteString) { implicit request: Request[ByteString] =>
      val vegetableOpt: Option[Vegetable] = decodeJsonToVegetable(request.body.utf8String)

      val a: Either[Result, Result] = for {
        vegetable <- vegetableOpt.toRight(BadRequest(""))
      } yield {
        vegetableService.save(vegetable)
        Ok(vegetableEncoder(vegetable))
      }
      a.merge

    }

  def getByName(name: String) =
    Action { implicit request: Request[AnyContent] =>
      val vegetable = vegetableService.getByName(name).getOrElse(throw new RuntimeException("not found"))

      Ok(vegetableEncoder(vegetable))
    }

}

object VegetableController {

  import io.circe.Encoder
  import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
  case class Response(str: String)
  val responseEncoder: Encoder[Response]            = deriveEncoder
  val vegetableEncoder: Encoder[Vegetable]          = deriveEncoder
  implicit val vegetableDecoder: Decoder[Vegetable] = deriveDecoder
  val decodeJsonToVegetable: String => Option[Vegetable] =
    parser.parse(_).flatMap(_.as[Vegetable]).toOption
}
