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
    "testbed" -> "Testbed",
    "offline" -> "Offline",
    "ai" -> "AI Test",
    "multiplayer" -> "Multiplayer Test",
    "fixed" -> "Fixed Gem Stream",
    "allRed" -> "All Red",
    "allGreen" -> "All Green",
    "allBlue" -> "All Blue",
    "allYellow" -> "All Yellow",
    "allRedBlue" -> "All Red and Blue",
    "allCrash" -> "All Crash",
    "allWild" -> "All Wild"
  )

  def newInstance(id: UUID, scenario: String, seed: Int, players: Seq[PlayerRecord]) = {
    val playerNames = players.map(_.name).distinct
    if (playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }

    scenario match {
      case "normal" | "multiplayer" =>
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed)))
        ps.foreach(_.activeGemsCreate())
        Brawl(id, scenario, seed, ps)
      case "ai" =>
        val (w, h) = Constants.Board.defaultWidth -> Constants.Board.defaultHeight
        val ais = (0 until (8 - players.size)).map(i => Player(UUID.randomUUID, "AI " + i, Board("AI " + i, w, h), GemStream(seed), script = Some("tutorial")))
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed))) ++ ais
        ps.foreach(_.activeGemsCreate())
        val brawl = Brawl(id, "ai", seed, ps)
        brawl
      case "fixed" =>
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
      case x if x.startsWith("all") =>
        val testName = x.stripPrefix("all")
        val (w, h) = Constants.Board.defaultWidth -> Constants.Board.defaultHeight
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, w, h), MockGemStreams.forString(testName)))
        val brawl = Brawl(id, scenario, seed, ps)
        brawl.players.foreach(_.activeGemsCreate())
        brawl
      case "testbed" => ScenarioTestHelper.testbedBrawl(id, seed, players)
      case x if x.startsWith("test") => ScenarioTestHelper.testBrawl(id, scenario.stripPrefix("test"), seed, players)
      case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
    }
  }
}
