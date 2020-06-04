package controllers

import javax.inject._
import play.api.mvc._
import akka.util.ByteString

import io.circe.Json
import controllers.util.CirceWritable._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VegetableController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  import HomeController._

  def apple() =
    Action { implicit request: Request[AnyContent] =>
      Ok("")
    }

  def banana() =
    Action(parse.byteString) { implicit request: Request[ByteString] =>
      val str: String = request.body.utf8String // POSTのボディ
      // これがあればあとはDecoderを定義すればなんとでもJsonを扱える

      val response   = Response(str)
      val json: Json = responseEncoder(response)

      Ok(json)
    }
}

object HomeController {

  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  case class Response(str: String)
  val responseEncoder: Encoder[Response] = deriveEncoder

}
