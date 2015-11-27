package services.brawl

import java.util.UUID

import akka.actor.{ ActorRef, Props }
import utils.Logging
import utils.metrics.InstrumentedActor

object BrawlService {
  def props(scenario: String, players: Seq[(UUID, String, ActorRef)], seed: Int) = Props(classOf[BrawlService], scenario, players, seed)
}

case class BrawlService(scenario: String, players: Seq[(UUID, String, ActorRef)], seed: Int) extends InstrumentedActor with Logging {
  log.info(s"Started brawl scenario [$scenario] for [${players.map(p => p._1 + ": " + p._2)}] with seed [$seed].")

  override def receiveRequest = {
    case x => throw new IllegalArgumentException(s"Brawl service received unknown message [$x].")
  }
}
