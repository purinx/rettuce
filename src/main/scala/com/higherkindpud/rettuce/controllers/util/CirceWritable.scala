package controllers.util

import play.api.http.{ContentTypeOf, Writeable}
import akka.util.ByteString
import io.circe.Json

object CirceWritable {

  implicit def jsonContentType: ContentTypeOf[Json] = {
    ContentTypeOf(Some("application/json"))
  }
  implicit def jsonWriteable: Writeable[Json] = {
    Writeable.apply[Json](json => ByteString(json.spaces2))(jsonContentType)
  }

}
