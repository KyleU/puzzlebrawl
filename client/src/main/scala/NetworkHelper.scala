import json.{ BaseSerializers, ResponseMessageSerializers }
import models._
import utils.NetworkSocket

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.timers._

trait NetworkHelper { this: PuzzleBrawl =>
  val connectionEl = org.scalajs.dom.document.getElementById("status-connection")

  lazy val networkStatus = scenario match {
    case "Offline" => "offline"
    case "Normal" => "proxy"
    case _ => "proxy" //"blend"
  }

  protected[this] val socket = if (networkStatus == "offline") {
    None
  } else {
    val s = new NetworkSocket(onSocketConnect, onSocketMessage, onSocketError, onSocketClose)
    s.open()
    Some(s)
  }

  protected[this] var sendCallback: js.Function1[String, Unit] = _

  private def sendPing(): Unit = {
    socket.foreach { s =>
      if (s.connected) {
        s.send(s"""{ "c": "Ping", "v": { "timestamp": ${System.currentTimeMillis} } }""")
      }
    }
    setTimeout(10000)(sendPing())
  }

  setTimeout(1000)(sendPing())

  protected[this] def messageReceived(c: String, v: js.Dynamic) = if (networkStatus == "proxy") {
    this.socket match {
      case Some(sock) => sock.send(s"""{"c": "$c", "v": ${JSON.stringify(v)} }""")
      case None => throw new IllegalStateException()
    }
  } else if (networkStatus == "offline") {
    handleMessage(c, v)
  }

  protected[this] def send(rm: ResponseMessage): Unit = {
    val json = ResponseMessageSerializers.write(rm)
    val s = BaseSerializers.write(json)
    sendCallback(s)
  }

  protected[this] def onSocketConnect(): Unit = {
    connectionEl.classList.remove("error")
    connectionEl.classList.add("connected")

    activeBrawl match {
      case Some(b) => throw new IllegalStateException("TODO: Reconnect.")
      case None => if (this.pendingStart) {
        start()
      }
    }
  }

  protected[this] def onSocketError(error: String): Unit = {
    connectionEl.classList.remove("connected")
    connectionEl.classList.add("error")
  }

  protected[this] def onSocketClose(): Unit = {
    connectionEl.classList.remove("connected")
    connectionEl.classList.add("error")
  }

  protected[this] def onSocketMessage(s: String): Unit = networkStatus match {
    case "offline" => throw new IllegalStateException()
    case "proxy" => sendCallback(s)
    case "blend" => scala.scalajs.js.Dynamic.global.console.log(s"Message [$s] received from socket.") // TODO
  }
}
