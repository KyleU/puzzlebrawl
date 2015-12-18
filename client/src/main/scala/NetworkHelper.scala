import json.{ BaseSerializers, JsonUtils, ResponseMessageSerializers }
import models._
import utils.NetworkSocket

import scala.scalajs.js
import scala.scalajs.js.JSON

trait NetworkHelper { this: PuzzleBrawl =>
  lazy val networkStatus = scenario match {
    case "offline" => "offline"
    case "normal" => "proxy"
    case _ => "proxy" //"blend"
  }

  protected[this] val socket = if (networkStatus == "offline") {
    None
  } else {
    val s = new NetworkSocket(onSocketConnect, onSocketMessage)
    s.open()
    Some(s)
  }

  protected[this] var sendCallback: js.Function1[String, Unit] = _

  protected[this] def messageReceived(c: String, v: js.Dynamic) = if (networkStatus == "proxy") {
    this.socket match {
      case Some(sock) => sock.send(s"""{"c": "$c", "v": ${JSON.stringify(v)} }""")
      case None => throw new IllegalStateException()
    }
  } else if (networkStatus == "offline") {
    c match {
      case "GetVersion" => handleVersionResponse()
      case "Ping" => handlePing(JsonUtils.getLong(v.timestamp))
      case "StartBrawl" => handleStartBrawl(v.scenario.toString)
      case "DebugRequest" => handleDebugRequest(v.data.toString)

      case "ActiveGemsLeft" => activePlayer.foreach(p => p.activeGemsLeft().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsRight" => activePlayer.foreach(p => p.activeGemsRight().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsClockwise" => activePlayer.foreach(p => p.activeGemsClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsCounterClockwise" => activePlayer.foreach(p => p.activeGemsCounterClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsStep" => activePlayer.foreach(p => p.activeGemsStep().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
      case "ActiveGemsDrop" => activePlayer.foreach(p => send(PlayerUpdate(p.id, p.activeGemsDrop() +: p.board.fullTurn() :+ p.activeGemsCreate())))

      case _ => throw new IllegalStateException(s"Invalid message [$c].")
    }
  }

  protected[this] def send(rm: ResponseMessage): Unit = {
    val json = ResponseMessageSerializers.write(rm)
    sendCallback(BaseSerializers.write(json))
  }

  protected[this] def onSocketConnect(): Unit = activeBrawl match {
    case Some(b) => throw new IllegalStateException("TODO: Reconnect.")
    case None => if (this.pendingStart) {
      start()
    }
  }

  protected[this] def onSocketMessage(s: String) = networkStatus match {
    case "offline" => throw new IllegalStateException()
    case "proxy" => sendCallback(s)
    case "blend" => println(s"Message [$s] received from socket.") // TODO
  }
}
