package models.game.board

import models.game.gem.Gem

trait FuseHelper { this: Board =>
  def fuse() = {
    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def check(gem: Gem, x: Int, y: Int): Seq[(Gem, Int, Int)] = {
      encountered += gem.id

      val right = at(x + 1, y).filter(g => g.color == gem.color && !g.crash && g.timer.isEmpty && !encountered.contains(g.id))
      val rightResult = right.map(g => check(g, x + 1, y))

      val above = at(x, y + 1).filter(g => g.color == gem.color && !g.crash && g.timer.isEmpty && !encountered.contains(g.id))
      val aboveResult = above.map(g => check(g, x, y + 1))

      Seq((gem, x, y)) ++ rightResult.getOrElse(Seq.empty) ++ aboveResult.getOrElse(Seq.empty)
    }

    mapGems { (gem, x, y) =>
      if(gem.crash || gem.timer.isDefined) {
        Seq.empty
      } else if(encountered.contains(gem.id)) {
        Seq.empty
      } else {
        val gemRun = check(gem, x, y)

        // TODO Fuse.

        Seq.empty
      }
    }
  }
}
