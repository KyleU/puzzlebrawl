import java.util.UUID

import models._
import models.brawl.Brawl

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object PuzzleBrawl extends js.JSApp {
  private[this] val userId = UUID.randomUUID
  private[this] var activeBrawl: Option[Brawl] = None

  override def main() = {}

  private[this] var sendCallback: js.Function1[String, Unit] = _

  @JSExport
  def register(callback: js.Function1[String, Unit]) = {
    sendCallback = callback
  }

  @JSExport
  def receive(c: String, v: js.Dynamic): Unit = c match {
    case "GetVersion" => send(VersionResponse("0.0"))
    case "Ping" => send(Pong(JsonUtils.getLong(v.timestamp)))
    case "StartBrawl" => handleStartBrawl(v.scenario.toString)
    case _ => throw new IllegalStateException(s"Invalid message [$c].")
  }

  protected def send(rm: ResponseMessage): Unit = {
    val json = JsonSerializers.write(rm)
    sendCallback(JsonSerializers.write(json))
  }

  private[this] def handleStartBrawl(scenario: String) = {
    if(scenario != "offline") {
      throw new IllegalStateException(s"Can't handle scenario [$scenario].")
    }
    activeBrawl.foreach(b => b)
    val players = Seq(userId -> "Offline User")
    val brawl = Brawl.blank(UUID.randomUUID, players = players)
    activeBrawl = Some(brawl)
    send(BrawlJoined(brawl, 0))
  }
}
