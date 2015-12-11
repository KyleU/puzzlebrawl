import org.scalajs.dom.raw._

object NetworkSocket {
  def open() = openSocket(url)

  private[this] val url = {
    val loc = org.scalajs.dom.document.location
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    wsProtocol + "://" + loc.host + "/websocket"
  }

  private[this] def openSocket(url: String) = {
    val ws = new WebSocket(url)
    ws.onopen = { (event: Event) => onOpen(event) }
    ws.onerror = { (event: ErrorEvent) => onError(event) }
    ws.onmessage = { (event: MessageEvent) => onMessage(event) }
    ws.onclose = { (event: Event) => onClose(event) }
    ws
  }

  private[this] def onOpen(event: Event) = {
    event
  }

  private[this] def onError(event: ErrorEvent) = {
    event
  }

  private[this] def onMessage(event: MessageEvent) = {
    val msg = event.data.toString
    println(s"Message [$msg] received")
    event
  }

  private[this] def onClose(event: Event) = {
    event
  }
}
