package models.board

import models.board.mutation.{ UpdateSegment, Mutation }
import models.board.mutation.Mutation.RemoveGem
import models.gem.Gem

trait CrashHelper { this: Board =>
  def crash(): Seq[UpdateSegment] = {
    val ret = mapGems { (gem, x, y) =>
      if (gem.crash.exists(x => x)) {
        crashGem(gem, x, y)
      } else {
        Seq.empty
      }
    }
    ret.map(r => UpdateSegment(r))
  }

  private[this] def crashGem(gem: Gem, x: Int, y: Int) = {
    if (!gem.crash.exists(x => x)) {
      throw new IllegalStateException(s"Crash called at [$x, $y], which is occupied by non-crash gem $gem.")
    }

    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def check(source: Gem, gem: Gem, x: Int, y: Int): Seq[(Gem, Int, Int)] = {
      if (gem.timer.isEmpty && gem.color == source.color && !encountered.contains(gem.id)) {
        encountered += gem.id
        def helper(xIdx: Int, yIdx: Int) = at(xIdx, yIdx).map(g => check(source, g, xIdx, yIdx)).getOrElse(Seq.empty)
        val (startX, startY) = startIndexFor(gem, x, y)

        val above = (0 until gem.width.getOrElse(1)).flatMap(w => helper(startX + w, startY + gem.height.getOrElse(1)))
        val right = (0 until gem.height.getOrElse(1)).flatMap(h => helper(startX + gem.width.getOrElse(1), startY + h))
        val below = (0 until gem.width.getOrElse(1)).flatMap(w => helper(startX + w, startY - 1))
        val left = (0 until gem.height.getOrElse(1)).flatMap(h => helper(startX - 1, startY + h))
        (gem, x, y) +: Seq(above, right, below, left).flatten
      } else {
        Seq.empty
      }
    }

    val run = check(gem, gem, x, y)
    if (run.size > 1) {
      run.map(n => applyMutation(RemoveGem(n._2, n._3)))
    } else {
      Seq.empty
    }
  }
}
