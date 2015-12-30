package services.brawl

import java.util.UUID

import models.{ MessageSet, ResponseMessage }
import org.joda.time.Seconds
import utils.Logging
import utils.metrics.InstrumentedActor

trait BrawlHelper
    extends InstrumentedActor
    with Logging
    with CheatHelper
    with ConnectionHelper
    with HistoryHelper
    with BrawlMessageHelper
    with InternalMessageHelper
    with TraceHelper
    with UpdateHelper { this: BrawlService =>

  protected[this] def elapsedSeconds = firstMoveMade.flatMap { first =>
    lastMoveMade.map { last =>
      Seconds.secondsBetween(first, last).getSeconds
    }
  }

  protected[this] def sendToAll(messages: Seq[ResponseMessage]): Unit = {
    if (messages.isEmpty) {
      log.info(s"No messages to send to all players for game [$scenario:$seed] in context [$context].")
    } else if (messages.tail.isEmpty) {
      sendToAll(messages.headOption.getOrElse(throw new IllegalStateException()))
    } else {
      sendToAll(MessageSet(messages))
    }
  }

  protected[this] def sendToAll(message: ResponseMessage): Unit = {
    players.foreach(_.connectionActor.foreach(_ ! message))
    observerConnections.foreach(_._1.connectionActor.foreach(_ ! message))
  }
}
