package services.supervisor

import java.util.UUID

import models._
import models.user.PlayerRecord
import services.brawl.BrawlService
import services.supervisor.ActorSupervisor.BrawlRecord
import utils.DateUtils

import scala.util.Random

trait ActorSupervisorBrawlHelper { this: ActorSupervisor =>
  private[this] def masterRng = new Random()

  protected[this] def handleCreateBrawl(scenario: String, players: Seq[UUID], seed: Option[Int]) {
    val id = UUID.randomUUID
    val finalSeed = Math.abs(seed.getOrElse(masterRng.nextInt()))

    val playerConnections = players.map(p => p -> connections(p))
    val playerRecords = playerConnections.map { pc =>
      PlayerRecord(pc._2.userId, pc._2.name, Some(pc._1), Some(pc._2.actorRef))
    }
    val actor = context.actorOf(BrawlService.props(id, scenario, playerRecords, finalSeed), s"game:$id")

    playerConnections.foreach(c => c._2.activeBrawl = Some(id))
    brawls(id) = BrawlRecord(playerConnections.map(c => c._1 -> c._2.name), actor, DateUtils.now)
    brawlsCounter.inc()
  }

  protected[this] def handleConnectionBrawlJoin(id: UUID, connectionId: UUID) = brawls.get(id) match {
    case Some(g) =>
      val c = connections(connectionId)
      log.info(s"Joining brawl [$id] as connection [$connectionId] for user [:].")
      c.activeBrawl = Some(id)
      g.actorRef ! AddPlayer(c.userId, c.name, connectionId, c.actorRef)
    case None =>
      log.warn(s"Attempted to join invalid brawl [$id].")
      sender() ! ServerError("Invalid Brawl", id.toString)
  }

  protected[this] def handleConnectionBrawlObserve(id: UUID, connectionId: UUID, as: Option[UUID]) = {
    brawls.get(id) match {
      case Some(b) =>
        log.info(s"Connection [$connectionId] is observing brawl [$id].")
        val c = connections(connectionId)
        c.activeBrawl = Some(id)
        c.actorRef ! BrawlStarted(id, b.actorRef, b.started)
        b.actorRef ! AddObserver(c.userId, c.name, connectionId, c.actorRef, as)
      case None =>
        log.warn(s"Attempted to observe invalid brawl [$id].")
        sender() ! ServerError("Invalid Brawl", id.toString)
    }
  }

  protected[this] def handleBrawlStopped(id: UUID) = brawls.remove(id) match {
    case Some(b) =>
      brawlsCounter.dec()
      b.connections.foreach { c =>
        connections.get(c._1).foreach { cr =>
          cr.activeBrawl = None
          cr.actorRef ! BrawlStopped(id)
        }
      }
      log.debug(s"Brawl [$id] stopped.")
    case None => log.warn(s"Attempted to stop missing brawl [$id].")
  }
}
