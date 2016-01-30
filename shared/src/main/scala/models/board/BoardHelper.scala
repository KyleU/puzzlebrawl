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
    val fuseActions = fuse()

    val comboOpt = if (combo == 1) { None } else { Some(combo) }
    val crashActions = crash(comboOpt)

    val collapseActions = collapse()

    val ret = Seq(
      fuseActions,
      crashActions,
      collapseActions.toSeq
    ).flatten

    if (ret.isEmpty) {
      carry
    } else {
      fullTurn(carry ++ ret, combo + 1)
    }
  }
}
