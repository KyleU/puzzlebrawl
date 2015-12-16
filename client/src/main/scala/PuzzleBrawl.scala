import java.util.UUID

import models._
import models.brawl.Brawl
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

@JSExport
class PuzzleBrawl {
  val scenario = {
    val hash = org.scalajs.dom.document.location.hash
    if (Option(hash).isEmpty || hash.isEmpty) {
      "normal"
    } else {
      hash.stripPrefix("#")
    }
  }

  val networkStatus = if (scenario == "offline") {
    "offline"
  } else {
    "proxy" // TODO "blend"
  }

  private[this] val userId = UUID.randomUUID
  private[this] var activeBrawl: Option[Brawl] = None
  private[this] var activePlayer: Option[Player] = None

  private[this] var pendingStart = false

  private[this] val socket = if (networkStatus == "offline") {
    None
  } else {
    val s = new NetworkSocket(onSocketConnect, onSocketMessage)
    s.open()
    Some(s)
  }

  private[this] var sendCallback: js.Function1[String, Unit] = _

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
  def receive(c: String, v: js.Dynamic): Unit = if (networkStatus == "proxy") {
    this.socket match {
      case Some(sock) => sock.send(s"""{"c": "$c", "v": ${JSON.stringify(v)} }""")
      case None => throw new IllegalStateException()
    }
  } else {
    c match {
      case "GetVersion" => send(VersionResponse("0.0"))
      case "Ping" => send(Pong(JsonUtils.getLong(v.timestamp)))
      case "StartBrawl" => handleStartBrawl(v.scenario.toString)

      case "ActiveGemsLeft" => activePlayer.foreach(p => p.activeGemsLeft().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsRight" => activePlayer.foreach(p => p.activeGemsRight().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsClockwise" => activePlayer.foreach(p => p.activeGemsClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsCounterClockwise" => activePlayer.foreach(p => p.activeGemsCounterClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsStep" => activePlayer.foreach(p => p.activeGemsStep().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsDrop" => activePlayer.foreach(p => send(PlayerUpdate(p.id, p.activeGemsDrop() +: p.board.fullTurn() :+ p.activeGemsCreate())))

      case "DebugRequest" => handleDebugRequest(v.data.toString)

      case _ => throw new IllegalStateException(s"Invalid message [$c].")
    }
  }

  protected def send(rm: ResponseMessage): Unit = {
    val json = ResponseMessageSerializers.write(rm)
    sendCallback(BaseSerializers.write(json))
  }

  protected def onSocketConnect(): Unit = activeBrawl match {
    case Some(b) => throw new IllegalStateException("TODO: Reconnect.")
    case None => if (this.pendingStart) {
      start()
    }
  }

  protected def onSocketMessage(s: String) = networkStatus match {
    case "offline" => throw new IllegalStateException()
    case "proxy" => sendCallback(s)
    case "blend" => println(s"Message [$s] received from socket.") // TODO
  }

  private[this] def handleStartBrawl(scenario: String) = {
    if (scenario != "offline") {
      throw new IllegalStateException(s"Can't handle scenario [$scenario].")
    }
    val players = Seq(userId -> "Offline User")
    val brawl = Brawl.blank(UUID.randomUUID, players = players)
    brawl.players.foreach(_.activeGemsCreate())
    activeBrawl = Some(brawl)
    activePlayer = brawl.players.find(p => p.id == userId)
    send(BrawlJoined(brawl, 0))
  }

  private[this] def handleDebugRequest(data: String) = data match {
    case "sync" => send(DebugResponse("sync", "Ok!"))
    case _ => throw new IllegalArgumentException(s"Unhandled debug request [$data].")
  }
}
