package services.supervisor

import java.util.UUID

import utils.Logging

case class Matchmaking() extends Logging {
  private[this] val queues = Map(
    "multiplayer" -> 2,
    "eight-way-brawl" -> 8
  ).mapValues(_ -> collection.mutable.ArrayBuffer.empty[UUID])

  def handlesScenario(scenario: String) = queues.get(scenario).isDefined

  def addPlayer(scenario: String, connectionId: UUID) = {
    val pending = queues.getOrElse(scenario, throw new IllegalStateException())
    val currentCount = pending._2.size
    if (currentCount + 1 == pending._1) {
      pending._2 += connectionId
      val players = Seq(pending._2: _*)
      log.info(s"Starting brawl for connections [${players.mkString(", ")}] for scenario [$scenario].")
      pending._2.clear()
      true -> players
    } else {
      pending._2 += connectionId
      log.info(s"Queueing connection [$connectionId] as player [${pending._2.size}] for scenario [$scenario].")
      false -> Seq(pending._2: _*)
    }
  }

  def connectionStopped(id: UUID) = queues.foreach { q =>
    if (q._2._2.contains(id)) {
      log.info(s"Removing connection [$id] from [${q._1}] queue.")
      q._2._2 -= id
    }
  }
}
