package services.brawl

import models._
import play.api.libs.json.Json
import utils.json.BrawlSerializers.brawlWrites

trait RequestHelper { this: BrawlService =>
  override def receiveRequest: PartialFunction[Any, Unit] = {
    case br: BrawlRequest => handleBrawlRequest(br)
    case im: InternalMessage => handleInternalMessage(im)
    case DebugRequest(data) => handleDebugRequest(data)
    case se: ServerError => handleServerError(se)
    case x => throw new IllegalArgumentException(s"Brawl service received unknown message [$x].")
  }

  private[this] def handleDebugRequest(data: String) = data match {
    case "sync" => sender() ! DebugResponse("sync", Json.prettyPrint(Json.toJson(brawl)))
    case x if x.startsWith("cheat-") => handleCheat(x.stripPrefix("cheat-"))
    case _ => log.warn(s"Unhandled debug request [$data] for brawl [$id].")
  }

  private[this] def handleCheat(key: String) = key match {
    case "victory" => sender() ! brawl.getCompletionReport
    case _ => log.error(s"Unknown cheat [$key].")
  }

  private[this] def handleServerError(se: ServerError) = {
    log.error(s"Server error enountered for brawl [$id]: ${se.reason} - ${se.content}")
  }
}
