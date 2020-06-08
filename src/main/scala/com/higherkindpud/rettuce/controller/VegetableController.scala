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
      val json: Json = vegetableEncoder(VegetableResponse("1", "apple", 500))
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
        vegetableService.create(vegetable)
        Ok(
          vegetableEncoder(
            VegetableResponse(vegetable.id.value.toString, vegetable.name, vegetable.price)
          )
        )
      }
      a.merge

    }

  def getByName(name: String) =
    Action { implicit request: Request[AnyContent] =>
      // val vegetable = vegetableService.getByName(name).getOrElse(throw new RuntimeException("not found"))
      val vegetable = ???

      Ok(vegetableEncoder(vegetable))
    }

}

object VegetableController {
  case class VegetableRequest(name: String, price: Int)

  case class VegetableResponse(id: String, name: String, price: Int)
  import io.circe.Encoder
  import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
  case class Response(str: String)
  val responseEncoder: Encoder[Response]                   = deriveEncoder
  val vegetableEncoder: Encoder[VegetableResponse]         = deriveEncoder
  implicit val vegetableDecoder: Decoder[VegetableRequest] = deriveDecoder
  val decodeJsonToVegetable: String => Option[Vegetable] =
    parser
      .parse(_)
      .flatMap(_.as[VegetableRequest])
      .toOption
      .map(req => Vegetable.create(req.name, req.price))
}
