package models.gem

import models.gem.StaticGemPatterns._
import models.player.Player

import scala.util.Random

object GemPattern {
  val default = horizontal

  lazy val all = Seq(horizontal, vertical, boxes, foolish, grid, invaders, rainfall, rampart, tetrino, slip, slide)

  def fromString(s: String) = all.find(_.key == s).getOrElse(throw new IllegalArgumentException())
}

final case class GemPattern(key: String, rows: Seq[Seq[Color]]) {
  val height = rows.length
  val width = rows.map(_.length).max

  if (height != 4 || width != 6) {
    throw new IllegalStateException("Invalid pattern size [, ].")
  }

  def at(x: Int, y: Int) = rows(y)(x)

  def applyCharge(deltas: Seq[Double], target: Player, rng: Random) = {
    val gems = deltas.flatMap { delta =>
      val startingColIndex = rng.nextInt(width)
      (0 until delta.toInt).map { i =>
        val newGem = target.gemStream.nextTimer(color = Color.allColors(rng.nextInt(4)))
        GemLocation(newGem, (startingColIndex + i) % width, 0)
      }
    }
    target.pendingGems = target.pendingGems ++ gems
  }
}
