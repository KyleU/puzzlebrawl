package models.game.board

import models.game.board.mutation.Mutation
import models.game.board.mutation.Mutation.RemoveGem
import models.game.gem.Gem

trait CrashHelper { this: Board =>
  def crash(): Seq[Seq[Mutation]] = mapGems { (gem, x, y) =>
    if (gem.crash) {
      crashGem(gem, x, y)
    } else {
      Seq.empty
    }
  }

  private[this] def crashGem(gem: Gem, x: Int, y: Int) = {
    if (!gem.crash) {
      throw new IllegalStateException(s"Crash called at [$x, $y], which is occupied by non-crash gem $gem.")
    }

    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def check(source: Gem, gem: Gem, x: Int, y: Int): Seq[(Gem, Int, Int)] = {
      if (gem.timer.isEmpty && gem.color == source.color && !encountered.contains(gem.id)) {
        def helper(xIdx: Int, yIdx: Int) = at(xIdx, yIdx).map(g => check(source, g, xIdx, yIdx)).getOrElse(Seq.empty)
        encountered += gem.id
        (gem, x, y) +: Seq(
          (0 until width).map(w => helper(x + w, y + 1)),
          (0 until height).map(h => helper(x + 1, y + h)),
          (0 until width).map(w => helper(x + w, y - 1)),
          (0 until height).map(h => helper(x - 1, y + h))
        ).flatten.flatten
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
