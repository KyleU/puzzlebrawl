package services.brawl

import java.util.UUID

import akka.actor.{ Cancellable, Props }
import models._
import models.scenario.Scenario
import models.user.PlayerRecord
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import utils.DateUtils
import utils.json.BrawlSerializers.brawlWrites

object BrawlService {
  def props(id: UUID, scenario: String, players: Seq[PlayerRecord], seed: Int) = Props(classOf[BrawlService], id, scenario, players, seed)
}

case class BrawlService(id: UUID, scenario: String, players: Seq[PlayerRecord], seed: Int) extends BrawlHelper {
  protected[this] lazy val brawl = Scenario.newInstance(id, scenario, seed, players)

  protected[this] val observerConnections = collection.mutable.ArrayBuffer.empty[(PlayerRecord, Option[UUID])]

  protected[this] val brawlMessages = collection.mutable.ArrayBuffer.empty[(BrawlMessage, UUID, LocalDateTime)]
  protected[this] var moveCount = 0
  protected[this] var firstMoveMade: Option[LocalDateTime] = None
  protected[this] var lastMoveMade: Option[LocalDateTime] = None

  protected[this] var status = "started"
  private[this] var schedule: Option[Cancellable] = None

  override def preStart() = {
    log.info(s"Starting brawl scenario [$scenario] for [${players.map(p => p.userId + ": " + p.name).mkString(", ")}] with seed [$seed].")
    players.foreach { player =>
      player.connectionActor.foreach(_ ! BrawlStarted(brawl.id, self, DateUtils.fromMillis(brawl.started)))
      player.connectionActor.foreach(_ ! BrawlJoined(player.userId, brawl, 0))
    }
    insertHistory()
    schedule = {
      import scala.concurrent.duration._
      Some(context.system.scheduler.schedule(2.seconds, 100.milliseconds, self, BrawlUpdate))
    }
  }

  override def postStop() = schedule.map(_.cancel())

  override def receiveRequest = {
    case br: BrawlRequest => handleBrawlRequest(br)
    case im: InternalMessage => handleInternalMessage(im)
    case DebugRequest(data) => handleDebugRequest(data)
    case x => throw new IllegalArgumentException(s"Brawl service received unknown message [$x].")
  }

  private[this] def handleDebugRequest(data: String) = data match {
    case "sync" => sender() ! DebugResponse("sync", Json.prettyPrint(Json.toJson(brawl)))
    case _ => log.warn(s"Unhandled debug request [$data] for brawl [$id].")
  }
}
