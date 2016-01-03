import java.util.UUID

import json.{ BaseSerializers, RequestMessageSerializers }
import models._
import models.board.mutation.Mutation.TargetChanged
import models.board.mutation.UpdateSegment
import models.brawl.Brawl
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.Random

@JSExport
class PuzzleBrawl extends MessageHelper with NetworkHelper with Brawl.Callbacks {
  lazy val scenario = {
    val hash = org.scalajs.dom.document.location.hash
    if (Option(hash).isEmpty || hash.isEmpty) { "Normal" } else { hash.stripPrefix("#") }
  }

  protected[this] val userId = UUID.randomUUID // TODO
  protected[this] var activeBrawl: Option[Brawl] = None
  protected[this] def brawl = activeBrawl.getOrElse(throw new IllegalStateException("No active brawl."))
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

  override def onLoss(playerId: UUID) = {
    send(PlayerLoss(playerId))
    brawl.players.filter(_.target.contains(playerId)).foreach { p =>
      val validPlayers = Random.shuffle(brawl.players.filter(player => player.isActive && player.id != p.id).map(_.id))
      p.target = validPlayers.headOption
      p.target.foreach { tgt =>
        send(PlayerUpdate(p.id, Seq(UpdateSegment("target", Seq(TargetChanged(tgt))))))
      }
    }
  }

  override def onComplete() = send(brawl.getCompletionReport)
}
