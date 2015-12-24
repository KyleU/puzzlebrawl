import java.util.UUID

import json.{ BaseSerializers, RequestMessageSerializers }
import models._
import models.brawl.Brawl
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
class PuzzleBrawl extends NetworkHelper with MessageHelper {
  lazy val scenario = {
    val hash = org.scalajs.dom.document.location.hash
    if (Option(hash).isEmpty || hash.isEmpty) { "Normal" } else { hash.stripPrefix("#") }
  }

  protected[this] val userId = UUID.randomUUID // TODO
  protected[this] var activeBrawl: Option[Brawl] = None
  protected[this] var activePlayer: Option[Player] = None

  protected[this] var pendingStart = false

  @JSExport
  def register(callback: js.Function1[String, Unit]) = {
    sendCallback = callback
  }

  @JSExport
  def start() = networkStatus match {
    case "offline" => handleStartBrawl(scenario)
    case "proxy" => if (socket.exists(_.connected)) {
      val initialMessage = scenario match {
        case x if x.startsWith("observe") => RequestMessageSerializers.write(ObserveBrawl(UUID.fromString(x.substring(x.indexOf("-") + 1)), None))
        case x if x.startsWith("join") => RequestMessageSerializers.write(JoinBrawl(UUID.fromString(x.substring(x.indexOf("-") + 1))))
        case x => RequestMessageSerializers.write(StartBrawl(x))
      }
      socket.foreach(_.send(BaseSerializers.write(initialMessage)))
    } else {
      this.pendingStart = true
    }
    case "blend" => // TODO
  }

  @JSExport
  def receive(c: String, v: js.Dynamic): Unit = messageReceived(c, v)
}
