package services.brawl

import models.{ ActiveGemsStep, BrawlRequest }

trait UpdateHelper { this: BrawlService =>
  private[this] val startNanos = System.nanoTime
  private[this] var lastUpdateNanos = startNanos

  protected[this] def handleBrawlUpdate() = {
    val now = System.nanoTime
    val deltaMs = (now - lastUpdateNanos) / 1000000

    if (deltaMs > 1000) {
      //log.info(s"Update after [${deltaMs}ms] elapsed for brawl [$id].")
      brawl.players.foreach { player =>
        player.script match {
          case Some("basic") => self ! BrawlRequest(player.id, ActiveGemsStep)
          case Some(x) => throw new IllegalStateException(s"Unhandled script [$x].")
          case None => // No op
        }
      }
      lastUpdateNanos = now
    }
  }
}
