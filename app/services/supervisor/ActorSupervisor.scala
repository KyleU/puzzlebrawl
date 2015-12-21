package services.supervisor

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import models._
import models.user.User
import org.joda.time.LocalDateTime
import utils.{ ApplicationContext, Config, DateUtils, Logging }
import utils.metrics.{ InstrumentedActor, MetricsServletActor }

object ActorSupervisor extends Logging {
  case class BrawlRecord(connections: Seq[(UUID, String)], actorRef: ActorRef, started: LocalDateTime)
  case class ConnectionRecord(userId: UUID, name: String, actorRef: ActorRef, var activeBrawl: Option[UUID], started: LocalDateTime)
}

class ActorSupervisor(ctx: ApplicationContext) extends InstrumentedActor with Logging with ActorSupervisorBrawlHelper {
  import ActorSupervisor._

  protected[this] val connections = collection.mutable.HashMap.empty[UUID, ConnectionRecord]
  protected[this] val connectionsCounter = metrics.counter("active-connections")

  protected[this] val brawls = collection.mutable.HashMap.empty[UUID, BrawlRecord]
  protected[this] val brawlsCounter = metrics.counter("active-brawls")

  override def preStart() {
    context.actorOf(MetricsServletActor.props(ctx.config), "metrics-servlet")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }

  override def receiveRequest = {
    case cs: ConnectionStarted => timeReceive(cs) { handleConnectionStarted(cs.user, cs.connectionId, cs.conn) }
    case cs: ConnectionStopped => timeReceive(cs) { handleConnectionStopped(cs.connectionId) }

    case cb: CreateBrawl => timeReceive(cb) { handleCreateBrawl(cb.scenario, cb.connectionId, cb.seed) }
    case cbj: ConnectionBrawlJoin => timeReceive(cbj) { handleConnectionBrawlJoin(cbj.id, cbj.connectionId) }
    case cbo: ConnectionBrawlObserve => timeReceive(cbo) { handleConnectionBrawlObserve(cbo.id, cbo.connectionId, cbo.as) }
    case bs: BrawlStopped => timeReceive(bs) { handleBrawlStopped(bs.id) }

    case GetSystemStatus => timeReceive(GetSystemStatus) { handleGetSystemStatus() }
    case ct: ConnectionTrace => timeReceive(ct) { handleConnectionTrace(ct) }
    case ct: ClientTrace => timeReceive(ct) { handleClientTrace(ct) }
    case bt: BrawlTrace => timeReceive(bt) { handleBrawlTrace(bt) }

    case im: InternalMessage => log.warn(s"Unhandled internal message [${im.getClass.getSimpleName}] received.")
    case x => log.warn(s"ActorSupervisor encountered unknown message: ${x.toString}")
  }

  private[this] def handleGetSystemStatus() = {
    val brawlStatuses = brawls.toList.sortBy(_._1).map(x => x._1 -> x._2.connections)
    val connectionStatuses = connections.toList.sortBy(_._2.name).map(x => x._1 -> x._2.name)
    sender() ! SystemStatus(brawlStatuses, connectionStatuses)
  }

  private[this] def handleConnectionTrace(ct: ConnectionTrace) = connections.find(_._1 == ct.id) match {
    case Some(g) => g._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Connection", ct.id.toString)
  }

  private[this] def handleClientTrace(ct: ClientTrace) = connections.find(_._1 == ct.id) match {
    case Some(g) => g._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Client", ct.id.toString)
  }

  private[this] def handleBrawlTrace(bt: BrawlTrace) = brawls.get(bt.id) match {
    case Some(g) => g.actorRef forward bt
    case None => sender() ! ServerError("Unknown Game", bt.id.toString)
  }

  protected[this] def handleConnectionStarted(user: User, connectionId: UUID, conn: ActorRef) {
    log.debug(s"Connection [$connectionId] registered to [${user.username.getOrElse(user.id)}] with path [${conn.path}].")
    connections(connectionId) = ConnectionRecord(user.id, user.username.getOrElse("Guest"), conn, None, DateUtils.now)
    connectionsCounter.inc()
  }

  protected[this] def handleConnectionStopped(id: UUID) {
    connections.remove(id) match {
      case Some(conn) =>
        connectionsCounter.dec()
        conn.activeBrawl.foreach { bId =>
          brawls(bId).actorRef ! ConnectionStopped(id)
        }
        log.debug(s"Connection [$id] [${conn.actorRef.path}] stopped.")
      case None => log.warn(s"Connection [$id] stopped but is not registered.")
    }
  }
}
