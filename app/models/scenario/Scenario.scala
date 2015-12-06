package models.scenario

import java.util.UUID

import models.board.Board
import models.board.mutation.Mutation.AddGem
import models.brawl.Brawl
import models.gem.GemStream
import models.player.Player
import models.test.brawl.BrawlTest
import models.test.gem.MockGemStreams
import models.user.PlayerRecord

import scala.util.Random

object Scenario {
  def all = Seq(
    "testbed" -> "Testbed",
    "createMultiplayer" -> "Create Multiplayer",
    "joinMultiplayer" -> "Join Multiplayer",
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
      case "testbed" =>
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, 6, 12), GemStream(seed)))
        val brawl = Brawl(id, scenario, seed, ps)
        brawl.players.foreach { player =>
          (0 until 20).foreach { i =>
            val x = Random.nextInt(player.board.width)
            player.board.applyMutation(AddGem(player.gemStream.next, x, player.board.height - 1))
            player.board.drop(x, player.board.height - 1)
          }
          player.board.fullTurn()
          player.activeGemsCreate()
        }
        brawl
      case "normal" =>
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, 6, 12), GemStream(seed)))
        ps.foreach(_.activeGemsCreate())
        Brawl(id, scenario, seed, ps)
      case x if x.startsWith("test") =>
        val testName = x.stripPrefix("test")
        val provider = BrawlTest.fromString(testName).getOrElse(throw new IllegalArgumentException(s"Invalid test [$testName]."))
        val test = provider.newInstance(id)
        test.init()
        test.cloneOriginal()
        test.run()
        test.brawl
      case x if x.startsWith("all") =>
        val testName = x.stripPrefix("all")
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, 6, 12), MockGemStreams.forString(testName)))
        val brawl = Brawl(id, scenario, seed, ps)
        for (p <- brawl.players) {
          p.activeGemsCreate()
        }
        brawl
      case "ai" =>
        val ps = (0 until 8).map(i => Player(UUID.randomUUID, "User " + i, Board("User " + i, 6, 12), GemStream()))
        val id = UUID.randomUUID()
        val brawl = Brawl(id, "ai", seed, ps)
        brawl
      case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
    }
  }
}
