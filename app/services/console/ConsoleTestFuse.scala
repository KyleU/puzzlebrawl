package services.console

import com.googlecode.lanterna.input.{ KeyType, KeyStroke }
import models.game.{ Board, FuseRole, Gem }

object ConsoleTestFuse extends ConsoleTest {
  lazy val board = {
    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red), 0, 0)
    b.add(Gem(1, Gem.Red), 1, 0)
    b.add(Gem(2, Gem.Red), 2, 0)
    b.add(Gem(3, Gem.Red), 0, 1)
    b.add(Gem(4, Gem.Red), 1, 1)
    b.add(Gem(5, Gem.Red), 2, 1)
    b.add(Gem(6, Gem.Red), 0, 2)
    b.add(Gem(7, Gem.Red), 1, 2)
    b.add(Gem(8, Gem.Red), 2, 2)
    b.add(Gem(9, Gem.Green), 0, 3)
    b.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)
    b
  }

  lazy val test = {
    val b = Board(6, 12)
    b.add(Gem(0, Gem.Red, fuseRole = Some(FuseRole.BottomLeft)), 0, 0)
    b.add(Gem(1, Gem.Red, fuseRole = Some(FuseRole.Bottom)), 1, 0)
    b.add(Gem(2, Gem.Red, fuseRole = Some(FuseRole.BottomRight)), 2, 0)
    b.add(Gem(3, Gem.Red, fuseRole = Some(FuseRole.Left)), 0, 1)
    b.add(Gem(4, Gem.Red, fuseRole = Some(FuseRole.Center)), 1, 1)
    b.add(Gem(5, Gem.Red, fuseRole = Some(FuseRole.Right)), 2, 1)
    b.add(Gem(6, Gem.Red, fuseRole = Some(FuseRole.TopLeft)), 0, 2)
    b.add(Gem(7, Gem.Red, fuseRole = Some(FuseRole.Top)), 1, 2)
    b.add(Gem(8, Gem.Red, fuseRole = Some(FuseRole.TopRight)), 2, 2)
    b.add(Gem(9, Gem.Green), 0, 3)
    b.add(Gem(10, Gem.Blue, timer = Some(5)), 3, 0)
    b
  }

  override def init() = {
    client.add(board)
    client.add(test)
  }

  def fuse(b: Board) = {
    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def check(gem: Gem, x: Int, y: Int): Seq[(Gem, Int, Int)] = {
      encountered += gem.id

      val right = if(x == board.width - 1) {
        None
      } else {
        b.at(x + 1, y).filter(g => g.color == gem.color && !g.crash && g.timer.isEmpty && !encountered.contains(g.id))
      }
      val rightResult = right.map(g => check(g, x + 1, y))

      val above = if(y == board.height - 1) {
        None
      } else {
        b.at(x, y + 1).filter(g => g.color == gem.color && !g.crash && g.timer.isEmpty && !encountered.contains(g.id))
      }
      val aboveResult = above.map(g => check(g, x, y + 1))

      Seq((gem, x, y)) ++ rightResult.getOrElse(Seq.empty) ++ aboveResult.getOrElse(Seq.empty)
    }

    b.mapGems { (gem, x, y) =>
      if(gem.crash || gem.timer.isDefined) {
        Seq.empty
      } else if(encountered.contains(gem.id)) {
        Seq.empty
      } else {
        val gemRun = check(gem, x, y)
        client.addStatusLog(s"Encountered run [${gemRun.map(_._1.id).mkString(", ")}].")
        Seq.empty
      }
    }
  }

  override def inputCharacter(input: KeyStroke) = input match {
    case x if x.getKeyType == KeyType.Enter =>
      fuse(board)
      true
    case _ => false
  }
}
