package services.brawl

import java.util.UUID

import akka.actor.{ Cancellable, Props }
import models._
import models.scenario.Scenario
import models.user.PlayerRecord
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import utils.{ Config, DateUtils }
import utils.json.BrawlSerializers.brawlWrites

object BrawlService {
  def props(id: UUID, scenario: String, players: Seq[PlayerRecord], seed: Int, notificationCallback: (String) => Unit) = {
    Props(classOf[BrawlService], id, scenario, players, seed, notificationCallback)
  }
}

case class BrawlService(id: UUID, scenario: String, players: Seq[PlayerRecord], seed: Int, notificationCallback: (String) => Unit) extends BrawlHelper {
  protected[this] lazy val brawl = Scenario.newInstance(id, scenario, seed, players)

  protected[this] val playersById = players.map(x => x.userId -> x).toMap
  protected[this] val observerConnections = collection.mutable.ArrayBuffer.empty[(PlayerRecord, Option[UUID])]

  protected[this] val brawlMessages = collection.mutable.ArrayBuffer.empty[(BrawlMessage, UUID, LocalDateTime)]
  protected[this] var firstMoveMade: Option[LocalDateTime] = None
  protected[this] var lastMoveMade: Option[LocalDateTime] = None

  protected[this] var status = "started"

  override def preStart() = {
    log.info(s"Starting brawl scenario [$scenario] for [${players.map(p => p.userId + ": " + p.name).mkString(", ")}] with seed [$seed].")

    val startMessage = BrawlStarted(brawl.id, self, DateUtils.fromMillis(brawl.started))
    players.foreach { player =>
      player.connectionActor.foreach(_ ! startMessage)
      player.connectionActor.foreach(_ ! BrawlJoined(player.userId, brawl, 0))
    }

    insertHistory()

    val playersString = players.map(x => x.userId + " (" + x.name + ")").mkString(", ")
    val msg = s"Brawl `$id` started on `${Config.hostname}` with seed `$seed` using scenario `$scenario` for players `$playersString`."

    startSchedules()

    notificationCallback(msg)
  }

  override def postStop() = {
    val msg = s"Game completion report for `$id`:\n  " + players.map { p =>
      val bp = brawl.playersById(p.userId)
      s"`${p.userId}` (${p.name}): ${bp.board.getMoveCount} moves, ${bp.board.getNormalGemCount} normal gems, and ${bp.board.getTimerGemCount} timer gems."
    }.mkString("\n  ")
    notificationCallback(msg)
  }

  override def receiveRequest = {
    case br: BrawlRequest => handleBrawlRequest(br)
    case im: InternalMessage => handleInternalMessage(im)
    case DebugRequest(data) => handleDebugRequest(data)
    case se: ServerError => handleServerError(se)
    case x => throw new IllegalArgumentException(s"Brawl service received unknown message [$x].")
  }

  private[this] def handleDebugRequest(data: String) = data match {
    case "sync" => sender() ! DebugResponse("sync", Json.prettyPrint(Json.toJson(brawl)))
    case x if x.startsWith("cheat-") => handleCheat(x.stripPrefix("cheat-"))
    case _ => log.warn(s"Unhandled debug request [$data] for brawl [$id].")
  }

  private[this] def handleServerError(se: ServerError) = {
    log.error(s"Server error enountered for brawl [$id]: ${se.reason} - ${se.content}")
  }
}
