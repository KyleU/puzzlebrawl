package services.brawl

import models.BrawlJoined
import models.brawl.Brawl
import models.test.brawl.Test

import scala.util.Random

trait ScenarioHelper { this: BrawlService =>
  def newInstance(scenario: String, playerNames: Seq[String]) = scenario match {
    case "testbed" =>
      val brawl = Brawl.blank(playerNames = playerNames)
      brawl.players.foreach { player =>
        (0 until 20).foreach { i =>
          player.board.drop(player.gemStream.next, Random.nextInt(player.board.width))
        }
        player.board.fullTurn()
        player.createActiveGems()
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
    case x => throw new IllegalArgumentException(s"Invalid scenario [$scenario].")
  }
}
