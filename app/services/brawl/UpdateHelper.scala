package services.brawl

trait UpdateHelper { this: BrawlService =>
  var lastUpdateNanos = System.nanoTime

  protected[this] def handleBrawlUpdate() = {
    val now = System.nanoTime
    val deltaMs = (now - lastUpdateNanos) / 1000000

    //log.info(s"Update after [${deltaMs}ms] elapsed for brawl [$id].")
    lastUpdateNanos = now
  }
}
