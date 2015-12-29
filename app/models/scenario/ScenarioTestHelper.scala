package models.scenario

import java.util.UUID

import models.Constants
import models.board.Board
import models.board.mutation.Mutation.AddGem
import models.brawl.Brawl
import models.gem.GemStream
import models.player.Player
import models.test.brawl.BrawlTest
import models.user.PlayerRecord

import scala.util.Random

object ScenarioTestHelper {
  def testBrawl(id: UUID, testName: String, players: Seq[PlayerRecord]) = {
    val provider = BrawlTest.fromString(testName).getOrElse(throw new IllegalArgumentException(s"Invalid test [$testName]."))
    val test = provider.newInstance(id, players.headOption.getOrElse(throw new IllegalStateException).userId)
    test.init()
    test.cloneOriginal()
    test.run()
    test.brawl
  }

  def testbedBrawl(id: UUID, seed: Int, players: Seq[PlayerRecord]) = {
    val ps = players.map(p => Player(p.userId, p.name, Board(p.name, Constants.Board.defaultWidth, Constants.Board.defaultHeight), GemStream(seed)))
    val brawl = Brawl(id, "Testbed", seed, ps)
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
  }
}
