package utils.json

import models._
import play.api.libs.json._

import utils.json.BrawlSerializers.brawlWrites

object ResponseMessageSerializers {
  private[this] val serverErrorWrites = Json.writes[ServerError]
  private[this] val pongWrites = Json.writes[Pong]
  private[this] val versionResponseWrites = Json.writes[VersionResponse]
  private[this] val disconnectedWrites = Json.writes[Disconnected]

  private[this] val brawlJoinedWrites = Json.writes[BrawlJoined]

  implicit val responseMessageWrites = Writes[ResponseMessage] { r: ResponseMessage =>
    val json = r match {
      case se: ServerError => serverErrorWrites.writes(se)
      case p: Pong => pongWrites.writes(p)
      case vr: VersionResponse => versionResponseWrites.writes(vr)
      case SendDebugInfo => JsObject(Nil)
      case d: Disconnected => disconnectedWrites.writes(d)

      case bj: BrawlJoined => brawlJoinedWrites.writes(bj)

      case _ => throw new IllegalArgumentException(s"Unhandled ResponseMessage type [${r.getClass.getSimpleName}].")
    }
    JsObject(Seq("c" -> JsString(r.getClass.getSimpleName.replace("$", "")), "v" -> json))
  }

  val messageSetWrites = Writes[MessageSet] { ms: MessageSet =>
    JsObject(Seq("c" -> JsString("MessageSet"), "v" -> JsObject(Seq("messages" -> JsArray(ms.messages.map(responseMessageWrites.writes))))))
  }
}
