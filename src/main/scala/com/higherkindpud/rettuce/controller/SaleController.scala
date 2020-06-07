package com.higherkindpud.rettuce.controller

import javax.inject._
import play.api.mvc._
import akka.util.ByteString
import io.circe.{Decoder, Json, parser}
import com.higherkindpud.rettuce.controller.util.CirceWritable._
import com.higherkindpud.rettuce.domain.entity.{Report, Vegetable}
import javax.inject.Singleton
import play.api.mvc.{AbstractController, ControllerComponents}
import com.higherkindpud.rettuce.domain.service.SaleService

@Singleton
class SaleController(
    controllerComponents: ControllerComponents,
    saleService: SaleService
) extends AbstractController(controllerComponents) {

  import SaleController._

  // 前回精算以降の売り上げ精算
  def settle() =
    Action(parse.byteString) { implicit request: Request[ByteString] =>
      ???
    }
}

object SaleController {

  import io.circe.Encoder
  import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
  case class Response(str: String)
  val responseEncoder: Encoder[Response]         = deriveEncoder
  val vegetableEncoder: Encoder[Report]          = deriveEncoder
  implicit val vegetableDecoder: Decoder[Report] = deriveDecoder
  val decodeJsonToSale: String => Option[Report] = parser.parse(_).flatMap(_.as[Report]).toOption

}
