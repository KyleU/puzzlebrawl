package services.brawl

import java.util.UUID

import akka.actor.Props
import models._
import models.user.PlayerRecord
import org.joda.time.LocalDateTime
import utils.DateUtils

object BrawlService {
  def props(scenario: String, players: Seq[PlayerRecord], seed: Int) = Props(classOf[BrawlService], scenario, players, seed)
}

case class BrawlService(scenario: String, players: Seq[PlayerRecord], seed: Int) extends BrawlHelper {
  protected[this] lazy val brawl = {
    val playerNames = players.map(_.name).distinct
    if(playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }
    newInstance(scenario, playerNames)
  }

  protected[this] val observerConnections = collection.mutable.ArrayBuffer.empty[(PlayerRecord, Option[UUID])]

  protected[this] val brawlMessages = collection.mutable.ArrayBuffer.empty[(BrawlMessage, UUID, LocalDateTime)]
  protected[this] var moveCount = 0
  protected[this] var firstMoveMade: Option[LocalDateTime] = None
  protected[this] var lastMoveMade: Option[LocalDateTime] = None

  protected[this] var status = "started"

  override def preStart() = {
    log.info(s"Starting brawl scenario [$scenario] for [${players.map(p => p.userId + ": " + p.name).mkString(", ")}] with seed [$seed].")
    players.foreach { player =>
      player.connectionActor.foreach(_ ! BrawlStarted(brawl.id, self, DateUtils.fromMillis(brawl.started)))
      player.connectionActor.foreach(_ ! BrawlJoined(brawl, 0))
    }
  }

  override def receiveRequest = {
    case br: BrawlRequest => handleBrawlRequest(br)
    case im: InternalMessage => handleInternalMessage(im)
    case x => throw new IllegalArgumentException(s"Brawl service received unknown message [$x].")
  }
}
