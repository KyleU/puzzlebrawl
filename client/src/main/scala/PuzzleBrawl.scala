import java.util.UUID

import json.{ BaseSerializers, RequestMessageSerializers }
import models._
import models.brawl.Brawl
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
class PuzzleBrawl extends NetworkHelper with MessageHelper {
  val scenario = {
    val hash = org.scalajs.dom.document.location.hash
    if (Option(hash).isEmpty || hash.isEmpty) {
      "normal"
    } else {
      hash.stripPrefix("#")
    }
  }

  protected[this] val userId = UUID.randomUUID
  protected[this] var activeBrawl: Option[Brawl] = None
  protected[this] var activePlayer: Option[Player] = None

  protected[this] var pendingStart = false

  @JSExport
  def register(callback: js.Function1[String, Unit]) = {
    sendCallback = callback
  }

  @JSExport
  def start() = networkStatus match {
    case "offline" => handleStartBrawl("offline")
    case "proxy" => if (socket.exists(_.connected)) {
      val json = RequestMessageSerializers.write(StartBrawl(scenario))
      socket.foreach(_.send(BaseSerializers.write(json)))
    } else {
      this.pendingStart = true
    }
    case "blend" => // TODO
  }

  @JSExport
  def receive(c: String, v: js.Dynamic): Unit = messageReceived(c, v)
}
