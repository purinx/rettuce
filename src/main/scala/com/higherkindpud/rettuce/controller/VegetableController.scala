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
    _controllerComponents: ControllerComponents,
    vegetableService: VegetableService
) extends BaseController {

  import VegetableController._

  def apple() =
    Action { implicit request: Request[AnyContent] =>
      val json: Json = vegetableEncoder(Vegetable("apple", 500))
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
      val vegetable: Vegetable = decodeJsonToVegetable(request.body.utf8String)
      vegetableService.save(vegetable)

      Ok(vegetableEncoder(vegetable))
    }

  def getByName(name: String) =
    Action { implicit request: Request[AnyContent] =>
      val vegetable = vegetableService.getByName(name).getOrElse(throw new RuntimeException("not found"))

      Ok(vegetableEncoder(vegetable))
    }

  override protected def controllerComponents: ControllerComponents = _controllerComponents
}

object VegetableController {

  import io.circe.Encoder
  import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
  case class Response(str: String)
  val responseEncoder: Encoder[Response]            = deriveEncoder
  val vegetableEncoder: Encoder[Vegetable]          = deriveEncoder
  implicit val vegetableDecoder: Decoder[Vegetable] = deriveDecoder
  val decodeJsonToVegetable: String => Vegetable =
    parser.parse(_).flatMap(_.as[Vegetable]).getOrElse(throw new RuntimeException("decode error"))
}
