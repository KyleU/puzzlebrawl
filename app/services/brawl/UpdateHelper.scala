package services.brawl

import models._

import scala.util.Random

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
          case Some("basic") =>
            val m = Random.nextInt(10) match {
              case i if i >= 0 && i <= 2 => ActiveGemsLeft
              case i if i >= 3 && i <= 5 => ActiveGemsRight
              case i if i >= 6 && i <= 6 => ActiveGemsClockwise
              case i if i >= 7 && i <= 7 => ActiveGemsCounterClockwise
              case i if i >= 8 && i <= 10 => ActiveGemsDrop
            }
            self ! BrawlRequest(player.id, m)
          case Some(x) => throw new IllegalStateException(s"Unhandled script [$x].")
          case None => // No op
        }
      }
      lastUpdateNanos = now
    }
  }
}
