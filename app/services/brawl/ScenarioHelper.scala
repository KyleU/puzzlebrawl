package services.brawl

import java.util.UUID

import models.board.Board
import models.brawl.Brawl
import models.gem.GemStream
import models.player.Player
import models.test.brawl.Test
import models.test.gem.MockGemStreams

import scala.util.Random

trait ScenarioHelper { this: BrawlService =>
  def newInstance(scenario: String) = {
    val playerNames = players.map(_.name).distinct
    if (playerNames.size != players.size) {
      throw new IllegalStateException(s"Players [${players.map(_.name).mkString(", ")}] contains a duplicate name.")
    }

    scenario match {
      case "testbed" =>
        val id = UUID.randomUUID()
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, 6, 12), GemStream(seed)))
        val brawl = Brawl(id, scenario, seed, ps)
        brawl.players.foreach { player =>
          (0 until 20).foreach { i =>
            player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
          }
          player.board.fullTurn()
          player.activeGemsCreate()
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
      case x if x.startsWith("all") =>
        val testName = x.stripPrefix("all")
        val id = UUID.randomUUID()
        val ps = players.map(p => Player(p.userId, p.name, Board(p.name, 6, 12), MockGemStreams.forString(testName)))
        val brawl = Brawl(id, scenario, seed, ps)
        for(p <- brawl.players) {
          p.activeGemsCreate()
        }
        brawl
      case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
    }
  }
}
