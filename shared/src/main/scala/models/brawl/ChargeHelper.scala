package models.brawl

import java.util.UUID

import models.gem.GemPattern

trait ChargeHelper { this: Brawl =>
  def applyCharge(id: UUID, deltas: Seq[Double]) = {
    val player = playersById(id)
    val pattern = GemPattern.fromString(player.gemPattern)

    val remainingDeltas = deltas.foldLeft(Seq.empty[Double]) { (x, d) =>
      if (player.pendingGems.length > d) {
        player.pendingGems = player.pendingGems.drop(d.toInt)
        x
      } else if (player.pendingGems.nonEmpty) {
        val remainder = d - player.pendingGems.length
        player.pendingGems = Seq.empty
        x :+ remainder
      } else {
        x :+ d
      }
    }

    player.target match {
      case Some(tgtId) => pattern.applyCharge(remainingDeltas, playersById(tgtId), rng)
      case None => // TODO No op for now
    }
  }
}
