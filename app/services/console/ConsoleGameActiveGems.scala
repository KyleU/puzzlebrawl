package services.console

import models.game.Game
import models.game.gem.GemLocation
import models.game.player.Player

class ConsoleGameActiveGems(game: Game, client: ConsoleClient) {
  def activeGemsLeft() = {
    client.getActivePlayer.activeGemsLeft()
    client.render()
  }

  def activeGemRight() = {
    client.getActivePlayer.activeGemsRight()
    client.render()
  }

  def stepActiveGems() = {
    client.getActivePlayer.activeGemsStep()
    client.render()
  }

  def activeGemsCounterClockwise() = {
    client.getActivePlayer.activeGemsCounterClockwise()
    client.render()
  }

  def activeGemsClockwise() = {
    client.getActivePlayer.activeGemsClockwise()
    client.render()
  }
}
