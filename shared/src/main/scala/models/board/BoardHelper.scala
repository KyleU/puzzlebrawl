package models.board

import models.board.mutation.UpdateSegment
import models.gem.Gem
import models.player.Player

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
  final def fullTurn(player: Player, carry: Seq[UpdateSegment] = Seq.empty, combo: Int = 1): Seq[UpdateSegment] = {
    val comboOpt = if (combo == 1) { None } else { Some(combo) }

    val timerActions = decrementTimers()

    val fuseActions = fuse()

    val wildsActions = processWilds()

    val crashActions = if (wildsActions.isEmpty) {
      crash(comboOpt)
    } else {
      Seq.empty
    }

    val postCrashFuseActions = if (wildsActions.isEmpty && crashActions.isEmpty) {
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
      crashActions,
      postCrashFuseActions,
      collapseActions.toSeq,
      postCollapseFuseActions
    ).flatten

    if (ret.isEmpty) {
      val scoreDelta = carry.flatMap(_.scoreDelta).sum
      player.score += scoreDelta

      carry
    } else {
      fullTurn(player, carry ++ ret, combo + 1)
    }
  }
}
