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
    "Testbed" -> "Testbed",
    "Offline" -> "Offline",
    "OneOnOne" -> "1v1 AI Test",
    "BasicAI" -> "Basic AI Test",
    "TeamAI" -> "Team AI Test",
    "Speedy" -> "Crazy Fast AI",
    "Spinner" -> "Spinning AI",
    "StressTest" -> "Stress Test",
    "Multiplayer" -> "Multiplayer Test",
    "EightWayBrawl" -> "Eight Player Multiplayer",
    "Fixed" -> "Fixed Gem Stream",
    "AllRed" -> "All Red",
    "AllGreen" -> "All Green",
    "AllBlue" -> "All Blue",
    "AllYellow" -> "All Yellow",
    "AllRedBlue" -> "All Red and Blue",
    "AllCrash" -> "All Crash"
  )

  def newInstance(id: UUID, scenario: String, seed: Int, players: Seq[PlayerRecord]) = {
    val playerNames = players.map(_.name).distinct
    if (playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }

    scenario match {
      case "Normal" | "Multiplayer" | "EightWayBrawl" =>
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
