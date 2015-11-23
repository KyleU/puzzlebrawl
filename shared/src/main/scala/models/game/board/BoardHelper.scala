package models.game.board

import models.game.board.mutation.Mutation
import models.game.gem.Gem

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

  final def fullTurn(): Seq[Seq[Mutation]] = {
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
      Seq(timerActions),
      fuseActions,
      wildsActions,
      postWildFuseActions,
      crashActions,
      postCrashFuseActions,
      Seq(collapseActions),
      postCollapseFuseActions
    ).flatten.filter(_.nonEmpty)

    if(ret.isEmpty) {
      ret
    } else {
      ret ++ fullTurn()
    }
  }
}
