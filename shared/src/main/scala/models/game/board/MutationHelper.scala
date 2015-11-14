package models.game.board

import models.game.board.Board._

trait MutationHelper { this: Board =>
  def applyMutations(mutations: Seq[Mutation]) = mutations.foreach(applyMutation)

  def applyMutation(m: Mutation): Unit = m match {
    case ag: AddGem => applyAdd(ag)
    case mg: MoveGem => applyMove(mg)
    case cg: ChangeGem => applyChange(cg)
    case fg: FuseGems => applyFuse(fg)
    case rg: RemoveGem => applyRemove(rg)
  }

  private[this] def applyAdd(m: AddGem) = spaces(m.x)(m.y) match {
    case Some(offender) => throw new IllegalStateException(s"Attempt to add [${m.gem}] to [${m.x}, ${m.y}], which is occupied by [$offender].")
    case None => spaces(m.x)(m.y) = Some(m.gem)
  }

  private[this] def applyChange(m: ChangeGem) = {
    val oldGem = spaces(m.x)(m.y).getOrElse(throw new IllegalStateException(s"Change attempted for empty position [${m.x}, ${m.y}]."))
    if(oldGem == m.newGem) {
      throw new IllegalStateException(s"Attempted to change unchanged gem [${m.newGem}].")
    }
    if(oldGem.id != m.newGem.id) {
      throw new IllegalStateException(s"Attempted to change [$oldGem] to gem [${m.newGem}] with a different id.")
    }
    spaces(m.x)(m.y) = Some(m.newGem)
  }

  private[this] def applyFuse(m: FuseGems) = for(y <- 0 until m.height; x <- 0 until m.width) yield {
    at(x, y) match {
      case None => throw new IllegalStateException(s"Fuse [$m] called for empty space at [$x, $y].")
      case Some(gem) if gem.group.contains(m.groupId) => None // No op
      case Some(gem) => applyMutation(ChangeGem(gem.copy(group = Some(m.groupId)), x, y))
    }
  }

  private[this] def applyMove(m: MoveGem) = {
    val source = spaces(m.oldX)(m.oldY).getOrElse(throw new IllegalStateException(s"Move attempted from empty position [${m.oldX}, ${m.oldY}]."))
    spaces(m.newX)(m.newY) match {
      case Some(offender) =>
        val msg = s"Attempted to move [$source] to [${m.newX}, ${m.newY}], which is occupied by [$offender]."
        throw new IllegalStateException(msg)
      case None =>
        spaces(m.oldX)(m.oldY) = None
        spaces(m.newX)(m.newY) = Some(source)
    }
  }

  private[this] def applyRemove(m: RemoveGem) = {
    val gem = spaces(m.x)(m.y).getOrElse(throw new IllegalStateException(s"Remove attempted from empty position [${m.x}, ${m.y}]."))
    if(!spaces(m.x)(m.y).contains(gem)) {
      throw new IllegalStateException(s"Attempt to remove [$gem] from empty location [${m.x}, ${m.y}].")
    }
    spaces(m.x)(m.y) = None
  }
}
