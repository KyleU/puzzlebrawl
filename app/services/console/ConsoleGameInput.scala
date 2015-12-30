package services.console

import com.googlecode.lanterna.input.{ KeyStroke, KeyType }
import models.board.mutation.Mutation.AddGem
import models.brawl.Brawl
import models.player.Player

import scala.util.Random

class ConsoleGameInput(brawl: Brawl, client: ConsoleClient) extends ConsoleInput {
  override def inputCharacter(input: KeyStroke): Boolean = input match {
    case x if x.getKeyType == KeyType.Enter =>
      brawl.players.foreach { player =>
        val x = Random.nextInt(player.board.width)
        player.board.applyMutation(AddGem(player.gemStream.next(), x, player.board.height - 1))
        player.board.drop(x, player.board.height - 1)
      }
      client.render()
      true
    case x if x.getKeyType == KeyType.ArrowLeft => withPlayer(_.activeGemsLeft())
    case x if x.getKeyType == KeyType.ArrowRight => withPlayer(_.activeGemsRight())
    case x if x.getKeyType == KeyType.ArrowUp => withPlayer(_.activeGemsClockwise())
    case x if x.getKeyType == KeyType.ArrowDown => withPlayer(_.activeGemsCounterClockwise())
    case x if x.getKeyType == KeyType.Character =>
      x.getCharacter match {
        case char if char.charValue == 'c' => brawl.players.foreach(_.board.collapse())
        case char if char.charValue == 'f' => brawl.players.foreach(_.board.fuse())
        case char if char.charValue == 'a' => withPlayer(_.activeGemsLeft())
        case char if char.charValue == 'd' => withPlayer(_.activeGemsRight())
        case char if char.charValue == 'w' => withPlayer(_.activeGemsClockwise())
        case char if char.charValue == 's' => withPlayer(_.activeGemsCounterClockwise())
        case char if char.charValue == '.' => withPlayer(_.activeGemsStep())
        case char if char.charValue == ' ' => withPlayer(_.dropActiveFullTurn)
        case char if char.charValue == '1' => client.setActivePlayer(brawl.players.headOption.getOrElse(throw new IllegalStateException()).id)
        case char if char.charValue == '2' => client.setActivePlayer(brawl.players(1).id)
        case char if char.charValue == '3' => client.setActivePlayer(brawl.players(2).id)
        case char if char.charValue == '4' => client.setActivePlayer(brawl.players(3).id)
        case char => client.addStatusLog(s"Unknown input: [$char].")
      }
      client.render()
      true
    case _ => true
  }

  private[this] def withPlayer(f: (Player) => Unit) = {
    f(client.getActivePlayer)
    client.render()
    true
  }
}
