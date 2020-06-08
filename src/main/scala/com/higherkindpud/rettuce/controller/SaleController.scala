package com.higherkindpud.rettuce.controller

import akka.util.ByteString
import com.higherkindpud.rettuce.domain.entity.Report
import com.higherkindpud.rettuce.domain.service.SaleService
import io.circe.{Decoder, parser}
import javax.inject.Singleton
import play.api.mvc.{AbstractController, ControllerComponents, _}

@Singleton
class SaleController(
    controllerComponents: ControllerComponents,
    saleService: SaleService
) extends AbstractController(controllerComponents) {

  // 前回精算以降の売り上げ精算
  def settle() =
    Action(parse.byteString) { implicit request: Request[ByteString] =>
      ???
    }
}

object SaleController {

  import io.circe.Encoder
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  case class Response(str: String)
  val responseEncoder: Encoder[Response]         = deriveEncoder
  val vegetableEncoder: Encoder[Report]          = deriveEncoder
  implicit val vegetableDecoder: Decoder[Report] = deriveDecoder
  val decodeJsonToSale: String => Option[Report] = parser.parse(_).flatMap(_.as[Report]).toOption

}
