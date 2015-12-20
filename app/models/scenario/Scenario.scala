package models.scenario

import java.util.UUID

import models.Constants
import models.board.Board
import models.brawl.Brawl
import models.gem.{ Color, Gem, GemStream }
import models.player.Player
import models.test.gem.MockGemStreams
import models.user.PlayerRecord

object Scenario {
  def all = Seq(
    "Testbed" -> "Testbed",
    "Offline" -> "Offline",
    "AI Test" -> "AI Test",
    "Multiplayer" -> "Multiplayer Test",
    "Fixed" -> "Fixed Gem Stream",
    "All Red" -> "All Red",
    "All Green" -> "All Green",
    "All Blue" -> "All Blue",
    "All Yellow" -> "All Yellow",
    "All Red/Blue" -> "All Red and Blue",
    "All Crash" -> "All Crash",
    "All Wild" -> "All Wild"
  )

  def newInstance(id: UUID, scenario: String, seed: Int, players: Seq[PlayerRecord]) = {
    val playerNames = players.map(_.name).distinct
    if (playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }

    scenario match {
      case "Normal" | "Multiplayer" =>
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed)))
        ps.foreach(_.activeGemsCreate())
        Brawl(id, scenario, seed, ps)
      case "AI Test" =>
        val (w, h) = Constants.Board.defaultWidth -> Constants.Board.defaultHeight
        val ais = (0 until (5 - players.size)).map(i => Player(UUID.randomUUID, "AI " + (i + 1), Board("AI " + (i + 1), w, h), GemStream(seed), script = Some("tutorial")))
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed))) ++ ais
        ps.foreach(_.activeGemsCreate())
        val brawl = Brawl(id, "AI Test", seed, ps)
        brawl
      case "Fixed" =>
        val ps = players.map { p =>
          val gs = new FixedGemStream(Seq(
            Gem(0), Gem(1),
            Gem(2, color = Color.Green), Gem(3, color = Color.Green, crash = Some(true)),
            Gem(4, color = Color.Blue), Gem(5, color = Color.Blue, crash = Some(true)),
            Gem(6, color = Color.Yellow), Gem(7, color = Color.Yellow, crash = Some(true)),
            Gem(8), Gem(9, crash = Some(true)),
            Gem(10), Gem(11)
          ))
          Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), gs)
        }
        ps.foreach(_.activeGemsCreate())
        Brawl(id, scenario, seed, ps)
      case x if x.startsWith("All ") =>
        val testName = x.stripPrefix("All ")
        val (w, h) = Constants.Board.defaultWidth -> Constants.Board.defaultHeight
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, w, h), MockGemStreams.forString(testName)))
        val brawl = Brawl(id, scenario, seed, ps)
        brawl.players.foreach(_.activeGemsCreate())
        brawl
      case "Testbed" => ScenarioTestHelper.testbedBrawl(id, seed, players)
      case x if x.startsWith("Test") => ScenarioTestHelper.testBrawl(id, scenario.stripPrefix("Test"), seed, players)
      case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
    }
  }
}
