package models.player

import models.board.mutation.Mutation.AddGem
import models.board.mutation.UpdateSegment
import models.gem.{ Gem, GemLocation }

trait ActiveGemHelper extends ActiveGemMoveHelper with ActiveGemRotationHelper { this: Player =>
  def activeGemsCreate(gemOne: Gem = gemStream.next, gemTwo: Gem = gemStream.next) = {
    if (activeGems.nonEmpty) {
      throw new IllegalStateException(s"Active gems created, but player [$id] already has active gems [${activeGems.mkString(", ")}].")
    }
    val x = (board.width / 2) - 1
    val y = board.height - 1
    if (!board.isValid(x, y)) {
      throw new IllegalStateException(s"Cannot create active gems, as [$x, $y] is occupied by [${board.at(x, y)}].")
    }
    if (!board.isValid(x + 1, y)) {
      throw new IllegalStateException(s"Cannot create active gems, as [${x + 1}, $y] is occupied by [${board.at(x + 1, y)}].")
    }
    val one = GemLocation(gemOne, x, y)
    val two = GemLocation(gemTwo, x + 1, y)
    activeGems = Seq(one, two)
    val msgs = Seq(
      this.board.applyMutation(AddGem(one.gem, x, y)),
      this.board.applyMutation(AddGem(two.gem, x + 1, y))
    )
    UpdateSegment("active-create", msgs)
  }

  def activeGemsDrop() = {
    val orderedGems = activeGems.sortBy(g => g.y -> g.x)
    activeGems = Seq.empty
    val msgs = orderedGems.flatMap(ag => board.drop(ag.x, ag.y))
    UpdateSegment("active-drop", msgs)
  }
}
