package services.console

import com.googlecode.lanterna.input.{ KeyStroke, KeyType }
import models.game.Game

import scala.util.Random

class ConsoleGameInput(game: Game, client: ConsoleClient, activeGems: ConsoleGameActiveGems) extends ConsoleInput {
  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      game.players.foreach { player =>
        player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
      }
      client.render()
      true
    case x if x.getKeyType == KeyType.ArrowLeft => activeGems.activeGemsLeft(); true
    case x if x.getKeyType == KeyType.ArrowRight => activeGems.activeGemRight(); true
    case x if x.getKeyType == KeyType.ArrowUp => activeGems.activeGemsClockwise(); true
    case x if x.getKeyType == KeyType.ArrowDown => activeGems.activeGemsCounterClockwise(); true
    case x if x.getKeyType == KeyType.Character =>
      x.getCharacter match {
        case char if char.charValue == 'c' => game.players.foreach(_.board.collapse())
        case char if char.charValue == 'f' => game.players.foreach(_.board.fuse())
        case char if char.charValue == 'a' => activeGems.activeGemsLeft()
        case char if char.charValue == 'd' => activeGems.activeGemRight()
        case char if char.charValue == '.' => activeGems.stepActiveGems()
        case char if char.charValue == ' ' =>
          val p = client.getActivePlayer
          p.dropActiveGems()
          p.board.fullTurn()
          p.createActiveGems()

        case char if char.charValue == '1' => client.setActivePlayer(game.players.headOption.getOrElse(throw new IllegalStateException()).id)
        case char if char.charValue == '2' => client.setActivePlayer(game.players(1).id)
        case char if char.charValue == '3' => client.setActivePlayer(game.players(2).id)
        case char if char.charValue == '4' => client.setActivePlayer(game.players(3).id)
        case char => client.addStatusLog(s"Unknown input: [$char].")
      }
      client.render()
      true
    case _ => true
  }
}
