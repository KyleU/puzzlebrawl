package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Board.MoveGem
import models.game.{ Board, Gem }

object ConsoleTestCollapse extends ConsoleTest {
  lazy val board = {
    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Green), 1, 2)
    b.add(Gem(2, Gem.Blue), 2, 4)
    b.add(Gem(3, Gem.Blue), 3, 7)
    b.add(Gem(4, Gem.Green), 4, 9)
    b.add(Gem(5, Gem.Red), 5, 11)

    b.add(Gem(6, Gem.Yellow), 2, 6)
    b.add(Gem(7, Gem.Yellow), 3, 9)
    b
  }

  lazy val test = {
    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Green), 1, 0)
    b.add(Gem(2, Gem.Blue), 2, 0)
    b.add(Gem(3, Gem.Blue), 3, 0)
    b.add(Gem(4, Gem.Green), 4, 0)
    b.add(Gem(5, Gem.Red), 5, 0)

    b.add(Gem(6, Gem.Yellow), 2, 1)
    b.add(Gem(7, Gem.Yellow), 3, 1)
    b
  }

  override def init() = {
    client.add(board)
    client.add(test)
  }

  def collapse(b: Board) = b.mapGems { (gem, x, y) =>
    val moveIndex = y match {
      case 0 => None
      case _ =>
        val occupied = ((y - 1) to 0 by -1).find { idx =>
          val space = b.at(x, idx)
          space.isDefined
        }
        occupied match {
          case Some(idx) if idx < y - 1 => Some(idx + 1)
          case Some(idx) => None
          case None => Some(0)
        }
    }
    moveIndex.map { idx =>
      val msg = MoveGem(x, y, x, idx)
      b.applyMutation(msg)
      msg
    }.toSeq
  }

  override def inputCharacter(input: KeyStroke) = input match {
    case x if x.getKeyType == KeyType.Enter =>
      val mutations = collapse(board)
      client.render()
      true
    case _ => true
  }
}
