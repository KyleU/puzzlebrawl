package models.player

import java.util.Date

import models.board.mutation.Mutation.{ MoveGems, MoveGem }
import models.gem.GemLocation

trait ActiveGemRotationHelper { this: Player =>
  def activeGemsClockwise() = activeGems match {
    case Seq(a, b) =>
      val delta = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> -1 // To the right
        case (0, -1) => -1 -> 1 // Below
        case (-1, 0) => 1 -> 1 // To the left
        case (0, 1) => 1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      setIfPossible(a, b, delta._1, delta._2)
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  }

  def activeGemsCounterClockwise() = activeGems match {
    case Seq(a, b) =>
      val delta = (b.x - a.x, b.y - a.y) match {
        case (1, 0) => -1 -> 1 // To the right
        case (0, -1) => 1 -> 1 // Below
        case (-1, 0) => 1 -> -1 // To the left
        case (0, 1) => -1 -> -1 // Above
        case (x, y) => throw new IllegalStateException(s"Unable to rotate active gems with unknown offset [$x, $y].")
      }
      setIfPossible(a, b, delta._1, delta._2)
    case _ => throw new IllegalStateException(s"There are [${activeGems.size}] active gems, but [2] are needed.")
  }

  private[this] def setIfPossible(a: GemLocation, b: GemLocation, bXDiff: Int, bYDiff: Int) = {
    val newActiveGems = closestMatch(a, b, bXDiff, bYDiff)

    newActiveGems.flatMap { newGems =>
      val mutations = newGems.flatMap { ng =>
        val og = activeGems.find(_.gem.id == ng.gem.id).getOrElse(throw new IllegalStateException())
        val delta = (ng.x - og.x) -> (ng.y - og.y)
        if (delta._1 != 0 || delta._2 != 0) {
          Some(MoveGem(og.x, og.y, delta._1, delta._2))
        } else {
          None
        }
      }
      if (mutations.isEmpty) {
        None
      } else {
        board.incrementMoveCount(new Date().getTime)
        val ret = board.applyMutation(MoveGems(mutations))
        activeGems = newGems
        Some(ret)
      }
    }
  }

  private[this] def closestMatch(a: GemLocation, b: GemLocation, bXDiff: Int, bYDiff: Int) = {
    def withDelta(xDelta: Int = 0, yDelta: Int = 0) = Seq(
      a.copy(x = a.x + xDelta, y = a.y + yDelta),
      b.copy(x = b.x + bXDiff + xDelta, y = b.y + bYDiff + yDelta)
    )

    val test = withDelta()
    val testOk = !test.exists(g => !board.isValid(g.x, g.y, activeGems))
    if (testOk) {
      Some(test)
    } else {
      val first = if (bXDiff > 0) { withDelta(xDelta = -1) } else { withDelta(xDelta = 1) }
      val firstOk = !first.exists(g => !board.isValid(g.x, g.y, activeGems))
      if (firstOk) {
        Some(first)
      } else {
        val second = if (bXDiff > 0) { withDelta(xDelta = 1) } else { withDelta(xDelta = -1) }
        val secondOk = !second.exists(g => !board.isValid(g.x, g.y, activeGems))
        if (secondOk) {
          Some(second)
        } else {
          val down = withDelta(yDelta = -1)
          val downOk = !down.exists(g => !board.isValid(g.x, g.y, activeGems))
          if (downOk) {
            Some(down)
          } else {
            Some(Seq(a.copy(gem = b.gem), b.copy(gem = a.gem)))
          }
        }
      }
    }
  }
}
