import java.util.UUID

import json.{ BaseSerializers, RequestMessageSerializers }
import models._
import models.brawl.{ PlayerResult, Brawl }
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
class PuzzleBrawl extends MessageHelper with NetworkHelper with Brawl.Callbacks {
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
        case x if x.startsWith("observe") => x.substring(x.indexOf("-") + 1) match {
          case id if id.contains('(') =>
            val gameId = UUID.fromString(id.substring(0, id.indexOf('(')))
            val as = UUID.fromString(id.substring(id.indexOf('(') + 1).dropRight(1))
            RequestMessageSerializers.write(ObserveBrawl(gameId, Some(as)))
          case id => RequestMessageSerializers.write(ObserveBrawl(UUID.fromString(id), None))
        }
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

  override def onLoss(playerId: UUID) = send(PlayerLoss(playerId))

  override def onComplete() = send(getCompletionReport)

  protected[this] def getCompletionReport = {
    val brawl = activeBrawl.getOrElse(throw new IllegalStateException())
    BrawlCompletionReport(
      id = brawl.id,
      scenario = brawl.scenario,
      durationMs = 0, // TODO
      results = brawl.players.map { p =>
        PlayerResult(
          id = p.id,
          name = p.name,
          script = p.script,
          team = p.team,
          score = p.score,
          normalGemCount = p.board.getNormalGemCount,
          timerGemCount = p.board.getTimerGemCount,
          moveCount = p.board.getMoveCount,
          status = p.status,
          completed = p.completed
        )
      }
    )
  }
}
