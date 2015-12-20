package models.board

import models.Constants
import models.board.mutation.Mutation.RemoveGem
import models.board.mutation.UpdateSegment
import models.gem.Gem

trait CrashHelper { this: Board =>
  def crash(combo: Option[Int]): Seq[UpdateSegment] = {
    val ret = mapGems { (gem, x, y) =>
      if (gem.crash.exists(x => x)) {
        crashGem(gem, x, y)
      } else {
        Seq.empty
      }
    }
    ret.flatMap {
      case r if r.isEmpty => None
      case r =>
        val base = r.map(x => pointsFor(x._1)).sum
        val comboBonus = comboBonusFor(combo)
        val gemBonus = (r.map(_._1).sum - 1) / 10
        val charge = Some(
          (base * Constants.Charging.normalGemCharge) +
          (gemBonus * Constants.Charging.bonusGemCharge) +
          (comboBonus * Constants.Charging.bonusGemCharge)
        )
        val scoreDelta = Some(
          (base * Constants.Scoring.normalGemScore) +
          (gemBonus * Constants.Scoring.bonusGemScore) +
          (comboBonus * Constants.Scoring.bonusGemScore)
        ).map(_.toInt)
        val mutations = r.map(_._2)
        Some(UpdateSegment("crash", mutations, combo = combo, charge = charge, scoreDelta = scoreDelta))
    }
  }

  private[this] def crashGem(gem: Gem, x: Int, y: Int) = {
    if (!gem.crash.exists(x => x)) {
      throw new IllegalStateException(s"Crash called at [$x, $y], which is occupied by non-crash gem $gem.")
    }

    val encountered = scala.collection.mutable.HashSet.empty[Int]

    def check(source: Gem, gem: Gem, x: Int, y: Int): Seq[(Gem, Int, Int)] = {
      if (!encountered.contains(gem.id) && source.timer.isEmpty && (gem.color == source.color || gem.timer.isDefined)) {
        encountered += gem.id
        def helper(xIdx: Int, yIdx: Int) = at(xIdx, yIdx).map(g => check(gem, g, xIdx, yIdx)).getOrElse(Seq.empty)
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
      run.map { n =>
        n._1.size -> applyMutation(RemoveGem(n._2, n._3))
      }
    } else {
      Seq.empty
    }
  }

  private[this] def pointsFor(size: Int) = size match {
    case x if x < 4 => size.toDouble
    case x if x < 9 => size * 2.0
    case x if x < 16 => size * 2.5
    case x if x < 25 => size * 3.0
    case x if x < 36 => size * 3.5
    case x if x < 49 => size * 4.0
    case _ => size * 5.0
  }

  private[this] def comboBonusFor(combo: Option[Int]) = combo match {
    case None | Some(1) => 0
    case Some(2) => 2
    case Some(3) => 4
    case Some(n) => 4 + ((n - 3) * 6)
  }
}
