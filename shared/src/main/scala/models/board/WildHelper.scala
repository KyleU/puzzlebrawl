package models.board

import models.board.mutation.Mutation
import models.board.mutation.Mutation.RemoveGem
import models.gem.Color

trait WildHelper { this: Board =>
  def processWilds(): Seq[Seq[Mutation]] = mapGems { (gem, x, y) =>
    if (gem.color == Color.Wild) {
      at(x, y - 1) match {
        case Some(seed) => applyMutation(RemoveGem(x, y)) +: mapGems { (candidate, candidateX, candidateY) =>
          if ((!candidate.crash.exists(x => x)) && candidate.timer.isEmpty && candidate.color == seed.color) {
            Seq(applyMutation(RemoveGem(candidateX, candidateY)))
          } else {
            Seq.empty
          }
        }.flatten
        case None => Seq(applyMutation(RemoveGem(x, y)))
      }
    } else {
      Seq.empty
    }
  }
}