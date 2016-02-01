package services.supervisor

import java.util.UUID

import utils.Logging

object Matchmaking {
  case class Queue(scenario: String, requiredPlayers: Int, var connections: Seq[UUID] = Seq.empty)
}

case class Matchmaking() extends Logging {
  private[this] val queues = Seq(
    Matchmaking.Queue("multiplayer", 2),
    Matchmaking.Queue("eight-way-brawl", 8)
  )
  private[this] val queueMap = queues.map(x => x.scenario -> x).toMap

  def handlesScenario(scenario: String) = queueMap.get(scenario).isDefined

  def getRequiredPlayerCount(scenario: String) = queueMap.get(scenario).map(_.requiredPlayers).getOrElse(0)

  def addPlayer(scenario: String, connectionId: UUID) = {
    val pending = queueMap.getOrElse(scenario, throw new IllegalArgumentException())
    log.info(queues.toString)
    val currentCount = pending.connections.size
    if (currentCount + 1 == pending.requiredPlayers) {
      val players = pending.connections :+ connectionId
      log.info(s"Starting brawl for connections [${players.mkString(", ")}] for scenario [$scenario].")
      pending.connections = Seq.empty
      true -> players
    } else {
      pending.connections = pending.connections :+ connectionId
      log.info(s"Queueing connection [$connectionId] as player [${pending.connections.size} of ${pending.requiredPlayers}] for scenario [$scenario].")
      false -> pending.connections
    }
  }

  def connectionStopped(id: UUID) = queues.find(_.connections.contains(id)).map { q =>
    q.connections = q.connections.filterNot(_ == id)
    log.info(s"Removed connection [$id] from [${q.scenario}] queue. [${q.connections.size}] players remain.")
    (q.scenario, q.connections)
  }
}
