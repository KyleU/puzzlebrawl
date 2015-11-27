package services.connection

import java.util.UUID

import akka.actor.{ ActorRef, Props }
import models._
import models.brawl.Brawl
import models.test.brawl.Test
import models.user.User
import utils.Config

import scala.util.Random

object ConnectionService {
  def props(supervisor: ActorRef, user: User, out: ActorRef) = Props(new ConnectionService(supervisor, user, out))
}

class ConnectionService(val supervisor: ActorRef, val user: User, val out: ActorRef) extends ConnectionServiceHelper {
  protected[this] val id = UUID.randomUUID

  protected[this] var userPreferences = user.preferences

  protected[this] var activeBrawlId: Option[UUID] = None
  protected[this] var activeBrawl: Option[ActorRef] = None

  protected[this] var pendingDebugChannel: Option[ActorRef] = None

  override def preStart() = {
    supervisor ! ConnectionStarted(user, id, self)
  }

  override def receiveRequest = {
    // Incoming basic messages
    case mr: MalformedRequest => timeReceive(mr) { log.error(s"MalformedRequest:  [${mr.reason}]: [${mr.content}].") }
    case p: Ping => timeReceive(p) { out ! Pong(p.timestamp) }
    case GetVersion => timeReceive(GetVersion) { out ! VersionResponse(Config.version) }
    case sp: SetPreference => timeReceive(sp) { handleSetPreference(sp) }
    case di: DebugInfo => timeReceive(di) { handleDebugInfo(di.data) }
    case sb: StartBrawl => timeReceive(sb) { handleStartBrawl(sb.scenario) }

    // Incoming game messages
    case im: InternalMessage => handleInternalMessage(im)

    // Outgoing messages
    case rm: ResponseMessage => handleResponseMessage(rm)

    case x => throw new IllegalArgumentException(s"Unhandled message [${x.getClass.getSimpleName}].")
  }

  override def postStop() = {
    supervisor ! ConnectionStopped(id)
  }

  private[this] def handleStartBrawl(scenario: String) = {
    val brawl = scenario match {
      case "testbed" =>
        val brawl = Brawl.blank(playerNames = Seq("a", "b", "c", "d"))
        brawl.players.foreach { player =>
          (0 until 20).foreach { i =>
            player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
          }
          player.board.fullTurn()
          player.createActiveGems()
        }
        brawl
      case x if x.startsWith("test") =>
        val testName = x.stripPrefix("test")
        val provider = Test.fromString(testName).getOrElse(throw new IllegalArgumentException(s"Invalid test [$testName]."))
        val test = provider.newInstance()
        test.init()
        test.cloneOriginal()
        test.run()
        test.brawl
      case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
    }
    out ! BrawlJoined(brawl, 0)
  }

  private[this] def handleInternalMessage(im: InternalMessage) = im match {
    case ct: ConnectionTrace => timeReceive(ct) { handleConnectionTrace() }
    case ct: ClientTrace => timeReceive(ct) { handleClientTrace() }
    case bm: BrawlMessage => handleBrawlMessage(bm)

    case x => throw new IllegalArgumentException(s"Unhandled internal message [${x.getClass.getSimpleName}].")
  }

  private[this] def handleBrawlMessage(bm: BrawlMessage) = activeBrawl match {
    case Some(ab) => ab forward BrawlRequest(user.id, bm)
    case None => throw new IllegalArgumentException(s"Received game message [${bm.getClass.getSimpleName}] while not in game.")
  }
}
