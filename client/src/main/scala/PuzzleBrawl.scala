import java.util.UUID

import models._
import models.brawl.Brawl
import models.player.Player

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object PuzzleBrawl extends js.JSApp {
  private[this] val userId = UUID.randomUUID
  private[this] var activeBrawl: Option[Brawl] = None
  private[this] var activePlayer: Option[Player] = None

  override def main() = {}

  private[this] var sendCallback: js.Function1[String, Unit] = _

  @JSExport
  def register(callback: js.Function1[String, Unit]) = {
    sendCallback = callback
  }

  @JSExport
  def receive(c: String, v: js.Dynamic): Unit = c match {
    case "GetVersion" => send(VersionResponse("0.0"))
    case "Ping" => send(Pong(JsonUtils.getLong(v.timestamp)))
    case "StartBrawl" => handleStartBrawl(v.scenario.toString)

    case "ActiveGemsLeft" => activePlayer.foreach(p => p.activeGemsLeft().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
    case "ActiveGemsRight" => activePlayer.foreach(p => p.activeGemsRight().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
    case "ActiveGemsClockwise" => activePlayer.foreach(p => p.activeGemsClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
    case "ActiveGemsCounterClockwise" => activePlayer.foreach(p => p.activeGemsCounterClockwise().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
    case "ActiveGemsStep" => activePlayer.foreach(p => p.activeGemsStep().foreach(m => send(PlayerUpdate.using(userId, "active", m))))
    case "ActiveGemsDrop" => activePlayer.foreach(p => send(PlayerUpdate(p.id, p.activeGemsDrop() +: p.board.fullTurn() :+ p.activeGemsCreate())))

    case _ => throw new IllegalStateException(s"Invalid message [$c].")
  }

  protected def send(rm: ResponseMessage): Unit = {
    val json = JsonSerializers.write(rm)
    sendCallback(JsonSerializers.write(json))
  }

  private[this] def handleStartBrawl(scenario: String) = {
    if(scenario != "offline") {
      throw new IllegalStateException(s"Can't handle scenario [$scenario].")
    }
    val players = Seq(userId -> "Offline User")
    val brawl = Brawl.blank(UUID.randomUUID, players = players)
    brawl.players.foreach(_.activeGemsCreate())
    activeBrawl = Some(brawl)
    activePlayer = brawl.players.find(p => p.id == userId)
    send(BrawlJoined(brawl, 0))
  }
}
