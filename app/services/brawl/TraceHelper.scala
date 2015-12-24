package services.brawl

import java.util.UUID

import models.TraceResponse

trait TraceHelper { this: BrawlService =>
  protected[this] def handleBrawlTrace() {
    def connUrl(id: UUID) = controllers.admin.routes.TraceController.traceConnection(id).url

    val playerStrings = players.map { player =>
      player.connectionId match {
        case Some(cId) =>
          val observeUrl = controllers.admin.routes.ObserveController.observeBrawlAs(brawl.id, player.userId).url
          val traceLink = s"""<a class="btn btn-default" href="${connUrl(cId)}" class="trace-link">Trace Connection</a>"""
          val observeLink = s"""<a class="btn btn-default" href="$observeUrl" target="_blank">Observe game as [${player.name}]</a>"""
          s"User:${player.userId}: ${player.name}<br />Connection: $cId<br />$traceLink$observeLink<br />"
        case None => s"${player.userId.toString} (Disconnected)"
      }
    }
    val ret = TraceResponse(brawl.id, List(
      "scenario" -> brawl.scenario,
      "seed" -> brawl.seed,
      "started" -> brawl.started,
      "players" -> playerStrings.mkString("<br/>\n"),
      "observers" -> observerConnections.map { x =>
        x._1.connectionId match {
          case Some(connId) => s"""<a href="${connUrl(connId)}" class="trace-link">$connId</a> (${x._1.name} as ${x._2.getOrElse("admin")})"""
          case None => s"${x._1.toString} (Disconnected)"
        }
      }.mkString("<br/>\n"),
      "messageCount" -> brawlMessages.size,
      "lastMessage" -> brawlMessages.lastOption.map(_.toString()).getOrElse("None")
    ))
    sender() ! ret
  }
}
