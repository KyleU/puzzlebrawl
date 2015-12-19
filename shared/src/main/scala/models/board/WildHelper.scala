package models.board

import models.Constants
import models.board.mutation.Mutation.RemoveGem
import models.board.mutation.UpdateSegment
import models.gem.Color

trait WildHelper { this: Board =>
  def processWilds() = {
    val ret = mapGems { (gem, x, y) =>
      if (gem.color == Color.Wild) {
        at(x, y - 1) match {
          case Some(seed) if seed.color == Color.Wild => Seq.empty
          case Some(seed) => applyMutation(RemoveGem(x, y)) +: mapGems { (candidate, candidateX, candidateY) =>
            if (candidate.color == seed.color) {
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
    ret.flatMap { r =>
      if (r.isEmpty) {
        None
      } else {
        val (charge, scoreDelta) = if(r.tail.isEmpty) {
          (Some(Constants.Charging.wildSoloDropCharge), Some(Constants.Scoring.wildSoloDropScore))
        } else {
          (Some((r.length * Constants.Charging.wildPerGemCharge).toInt), Some(r.length * Constants.Scoring.wildPerGemScore))
        }
        Some(UpdateSegment("wild", r, charge = charge, scoreDelta = scoreDelta))
      }
    }
  }
}
