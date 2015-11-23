package services.console

import models.game.Game
import models.game.gem.GemLocation
import models.game.player.Player

class ConsoleGameActiveGems(game: Game, client: ConsoleClient) {
  def activeGemLeft() = {
    val p = client.getActivePlayer
    val ok = !p.activeGems.exists { g =>
      if(g.x == 0) {
        true
      } else {
        p.board.at(g.x - 1, g.y).isDefined
      }
    }
    if(ok) {
      p.activeGems = p.activeGems.map(loc => loc.copy(x = loc.x - 1))
      client.render()
    }
  }

  def activeGemRight() = {
    val p = client.getActivePlayer
    val ok = !p.activeGems.exists { g =>
      if(g.x == p.board.width - 1) {
        true
      } else {
        p.board.at(g.x + 1, g.y).isDefined
      }
    }
    if(ok) {
      p.activeGems = p.activeGems.map(loc => loc.copy(x = loc.x + 1))
      client.render()
    }
  }

  def activeGemCounterClockwise() = {
    val p = client.getActivePlayer
    val newActive = p.activeGems match {
      case Seq(a, b) => (b.x - a.x, b.y - a.y) match {
        case (1, 0) => Seq(a, b.copy(x = b.x - 1, y = b.y + 1)) // To the right
        case (0, -1) => Seq(a, b.copy(x = b.x + 1, y = b.y + 1)) // Below
        case (-1, 0) => Seq(a, b.copy(x = b.x + 1, y = b.y - 1)) // To the left
        case (0, 1) => Seq(a, b.copy(x = b.x - 1, y = b.y - 1)) // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      case _ => throw new IllegalStateException(s"There are [${p.activeGems.size}] active gems, but [2] are needed.")
    }
    setActiveGems(p, newActive)
  }

  def activeGemClockwise() = {
    val p = client.getActivePlayer
    val newActive = p.activeGems match {
      case Seq(a, b) => (b.x - a.x, b.y - a.y) match {
        case (1, 0) => Seq(a, b.copy(x = b.x - 1, y = b.y - 1)) // To the right
        case (0, -1) => Seq(a, b.copy(x = b.x - 1, y = b.y + 1)) // Below
        case (-1, 0) => Seq(a, b.copy(x = b.x + 1, y = b.y + 1)) // To the left
        case (0, 1) => Seq(a, b.copy(x = b.x + 1, y = b.y - 1)) // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      case _ => throw new IllegalStateException(s"There are [${p.activeGems.size}] active gems, but [2] are needed.")
    }
    setActiveGems(p, newActive)
  }

  private[this] def setActiveGems(p: Player, newActive: Seq[GemLocation]) = {
    p.activeGems = newActive
    client.render()
  }
}
