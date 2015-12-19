package models.board

import models.board.mutation.UpdateSegment
import models.gem.Gem

import scala.annotation.tailrec

trait BoardHelper extends CollapseHelper with CrashHelper with DropHelper with FuseHelper with TimerHelper with WildHelper { this: Board =>
  @tailrec
  final def startIndexFor(gem: Gem, x: Int, y: Int): (Int, Int) = if (at(x - 1, y).contains(gem)) {
    startIndexFor(gem, x - 1, y)
  } else if (at(x, y - 1).contains(gem)) {
    startIndexFor(gem, x, y - 1)
  } else {
    (x, y)
  }

  @tailrec
  final def fullTurn(carry: Seq[UpdateSegment] = Seq.empty, combo: Int = 1): Seq[UpdateSegment] = {
    val timerActions = decrementTimers()

    val fuseActions = fuse()

    val wildsActions = processWilds()
    val postWildFuseActions = if (wildsActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    val crashActions = crash()
    val postCrashFuseActions = if (crashActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    val collapseActions = collapse()
    val postCollapseFuseActions = if (collapseActions.isEmpty) {
      Seq.empty
    } else {
      fuse()
    }

    val ret = Seq(
      timerActions.toSeq,
      fuseActions,
      wildsActions,
      postWildFuseActions,
      crashActions,
      postCrashFuseActions,
      collapseActions.toSeq,
      postCollapseFuseActions
    ).flatten

    if (ret.isEmpty) {
      carry
    } else {
      fullTurn(carry ++ ret, combo + 1)
    }
  }
}
