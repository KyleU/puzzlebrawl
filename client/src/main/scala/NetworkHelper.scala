import json.{ BaseSerializers, ResponseMessageSerializers }
import models._
import utils.{ Logging, NetworkSocket }

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.timers._

trait NetworkHelper { this: PuzzleBrawl =>
  val connectionEl = org.scalajs.dom.document.getElementById("status-connection")

  //val networkStatus = "proxy"
  //val networkStatus = "offline"
  val networkStatus = "blend"

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
    this.socket.foreach(_.send(c, v))
  } else if (networkStatus == "offline") {
    handleOfflineMessage(c, v)
  } else if (networkStatus == "blend") {
    handleBlendMessage(c, v)
  }

  protected[this] def send(rm: ResponseMessage): Unit = {
    val json = ResponseMessageSerializers.write(rm)
    val s = BaseSerializers.write(json)
    sendCallback(s)
  }

  protected[this] def onSocketConnect(): Unit = {
    connectionEl.classList.remove("error")
    connectionEl.classList.add("connected")
    onConnect()
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
    case "blend" => blendMessage(s)
  }

  private[this] def blendMessage(s: String) = {
    Logging.info(s"Message [$s] received from socket in blend mode.")
    sendCallback(s)
  }
}
