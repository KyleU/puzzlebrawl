package utils.json

import models._
import play.api.libs.json._

import utils.json.BrawlSerializers.{ brawlWrites, gemLocationWrites, playerResultWrites }
import utils.json.MutationSerializers.{ mutationWrites, updateSegmentWrites }

object ResponseMessageSerializers {
  private[this] val serverErrorWrites = Json.writes[ServerError]
  private[this] val pongWrites = Json.writes[Pong]
  private[this] val versionResponseWrites = Json.writes[VersionResponse]
  private[this] val debugResponseWrites = Json.writes[DebugResponse]
  private[this] val disconnectedWrites = Json.writes[Disconnected]

  private[this] val brawlJoinedWrites = Json.writes[BrawlJoined]

  private[this] val playerUpdateWrites = Json.writes[PlayerUpdate]
  private[this] val playerLossWrites = Json.writes[PlayerLoss]
  private[this] val brawlCompletionReportWrites = Json.writes[BrawlCompletionReport]

  implicit val responseMessageWrites = Writes[ResponseMessage] { r: ResponseMessage =>
    val json = r match {
      case se: ServerError => serverErrorWrites.writes(se)
      case p: Pong => pongWrites.writes(p)
      case vr: VersionResponse => versionResponseWrites.writes(vr)

      case SendTrace => JsObject(Nil)
      case dr: DebugResponse => debugResponseWrites.writes(dr)
      case d: Disconnected => disconnectedWrites.writes(d)

      case bj: BrawlJoined => brawlJoinedWrites.writes(bj)

      case pu: PlayerUpdate => playerUpdateWrites.writes(pu)
      case pl: PlayerLoss => playerLossWrites.writes(pl)
      case bcr: BrawlCompletionReport => brawlCompletionReportWrites.writes(bcr)

      case _ => throw new IllegalArgumentException(s"Unhandled ResponseMessage type [${r.getClass.getSimpleName}].")
    }
    JsObject(Seq("c" -> JsString(utils.Formatter.className(r)), "v" -> json))
  }

  val messageSetWrites = Writes[MessageSet] { ms: MessageSet =>
    JsObject(Seq("c" -> JsString("MessageSet"), "v" -> JsObject(Seq("messages" -> JsArray(ms.messages.map(responseMessageWrites.writes))))))
  }
}
