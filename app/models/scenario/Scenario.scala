package models.scenario

import java.util.UUID

import models.Constants
import models.board.Board
import models.brawl.Brawl
import models.gem.GemStream
import models.player.Player
import models.user.PlayerRecord

object Scenario {
  def all = Seq(
    "testbed" -> "Testbed",
    "offline" -> "Offline",
    "one-on-one" -> "1v1 AI Test",
    "basic-ai" -> "Basic AI Test",
    "team-ai" -> "Team AI Test",
    "speedy" -> "Crazy Fast AI",
    "spinner" -> "Spinning AI",
    "stress-test" -> "Stress Test",
    "multiplayer" -> "Multiplayer Test",
    "eight-way-brawl" -> "Eight Player Multiplayer",
    "fixed" -> "Fixed Gem Stream",
    "stream-red" -> "All Red",
    "stream-green" -> "All Green",
    "stream-blue" -> "All Blue",
    "stream-yellow" -> "All Yellow",
    "stream-red-blue" -> "All Red and Blue",
    "stream-crash" -> "All Crash"
  )

  def newInstance(id: UUID, scenario: String, seed: Int, players: Seq[PlayerRecord]) = {
    val playerNames = players.map(_.name).distinct
    if (playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }

    scenario match {
      case "normal" | "multiplayer" | "eight-way-brawl" =>
        val ps = players.zipWithIndex.map { p =>
          Player(p._1.userId, p._1.name, p._2, Board(p._1.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed))
        }
        ps.foreach(_.activeGemsCreate())
        Brawl(id, scenario, seed, ps)

      case "Testbed" => ScenarioBrawlHelper.testbedBrawl(id, seed, players)

      case _ => TestScenarios.newInstance(id, scenario, seed, players)
    }
  }
}
