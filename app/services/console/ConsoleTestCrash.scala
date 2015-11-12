package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.Board.{ RemoveGem, MoveGem }
import models.game.{ Board, Gem }

object ConsoleTestCrash extends ConsoleTest {
  lazy val board = {
    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Red, crash = true), 1, 0)
    b.add(Gem(2, Gem.Blue), 0, 1)
    b
  }

  lazy val test = {
    val b = Board(6, 12)
    b.add(Gem(2, Gem.Blue), 0, 0)
    b
  }

  override def init() = {
    client.add(board)
    client.add(test)
  }

  def crash(b: Board) = {
    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def neighbors(gem: Gem, x: Int, y: Int) = {

    }

    b.mapGems { (gem, x, y) =>
      if(gem.crash) {
        val msg = RemoveGem(x, y)
        b.applyMutation(msg)
        Seq(msg)
      } else {
        Seq.empty
      }
    }
  }

  override def inputCharacter(input: KeyStroke) = input match {
    case x if x.getKeyType == KeyType.Enter =>
      val mutations = crash(board)
      client.render()
      true
    case _ => true
  }
}
