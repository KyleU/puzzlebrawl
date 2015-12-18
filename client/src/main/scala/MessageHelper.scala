import java.util.UUID

import models._
import models.brawl.Brawl

trait MessageHelper { this: PuzzleBrawl =>
  protected[this] def handleVersionResponse() = send(VersionResponse("0.0"))

  protected[this] def handlePing(timestamp: Long) = send(Pong(timestamp))

  protected[this] def handleStartBrawl(scenario: String) = {
    if (scenario != "offline") {
      throw new IllegalStateException(s"Can't handle scenario [$scenario].")
    }
    val players = Seq(userId -> "Offline User")
    val brawl = Brawl.blank(UUID.randomUUID, players = players)
    brawl.players.foreach(_.activeGemsCreate())
    activeBrawl = Some(brawl)
    activePlayer = brawl.players.find(p => p.id == userId)
    send(BrawlJoined(userId, brawl, 0))
  }

  protected[this] def handleDebugRequest(data: String) = data match {
    case "sync" => send(DebugResponse("sync", "Ok!"))
    case _ => throw new IllegalArgumentException(s"Unhandled debug request [$data].")
  }
}
