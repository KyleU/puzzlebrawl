import models.RequestMessage
import org.scalajs.dom.raw._
import upickle.Js

class NetworkSocket(onConnect: () => Unit, onMessage: (String, Js.Obj) => Unit) {
  private[this] var connecting = false
  private[this] var connected = false
  private[this] var ws: Option[WebSocket] = None

  println("NetworkSocket started.")

  def open() = if(connected) {
    throw new IllegalStateException("Already connected.")
  } else if(connecting) {
    throw new IllegalStateException("Already connecting.")
  } else {
    openSocket(url)
  }

  def send(c: String, v: RequestMessage) {
    println("Send [" + c + "]!")
  }

  private[this] val url = {
    val loc = org.scalajs.dom.document.location
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    wsProtocol + "://" + loc.host + "/websocket"
  }

  private[this] def openSocket(url: String) = {
    connecting = true
    println(s"Connecting websocket to [$url].")
    val socket = new WebSocket(url)
    socket.onopen = { (event: Event) =>
      connecting = false
      connected = true
      onConnect()
      event
    }
    socket.onerror = { (event: ErrorEvent) =>
      println("Error!")
      event
    }
    socket.onmessage = { (event: MessageEvent) => onMessageEvent(event) }
    socket.onclose = { (event: Event) =>
      connecting = false
      connected = false
      println("Close!")
      event
    }
    ws = Some(socket)
  }

  private[this] def onMessageEvent(event: MessageEvent) = {
    val msg = event.data.toString
    println(s"Message [$msg] received.")
    onMessage("TODO", Js.Obj())
    event
  }
}
