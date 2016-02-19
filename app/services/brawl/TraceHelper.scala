package services.brawl

import models.BrawlTraceResponse

trait TraceHelper { this: BrawlService =>
  protected[this] def handleBrawlTrace() = {
    val ret = BrawlTraceResponse(
      id = brawl.id,
      scenario = brawl.scenario,
      seed = brawl.seed,
      started = brawl.started,
      players = players.map(p => (p.userId, p.name, p.connectionId, playerMessageCounts(p.userId), lastBrawlMessages(p.userId))),
      observers = observerConnections.map(c => (c._1.userId, c._1.name, c._2))
    )
    sender() ! ret
  }
}
