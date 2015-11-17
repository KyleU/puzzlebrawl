package services.console

import models.game.board.Board
import models.game.test.GameTest

object ConsoleTest {
  def main(args: Array[String]) {
    args.headOption.flatMap(GameTest.fromString) match {
      case Some(test) => run(test.newInstance(), args.contains("pause"))
      case x => throw new IllegalStateException(s"First argument must be one of [${GameTest.all.map(_.testName).mkString(", ")}].")
    }
  }

  def run(test: GameTest, pauseBeforeRun: Boolean) = {
    val testName = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("GameTest", "")
    test.init()

    val originalBoard = test.board.clone("original")

    val client = new ConsoleClient()
    client.add(originalBoard)
    client.add(test.board)
    client.add(test.goal)

    if(pauseBeforeRun) {
      client.addStatusLog(s"Test [$testName] initialized.")
      client.screen.readInput()
    }
    client.render()

    test.run()
    client.render()

    val errors = test.getErrors
    if(errors.isEmpty) {
      client.addStatusLog(s"Test [$testName] completed successfully. Press any key to continue.")
    } else {
      client.addStatusLog(s"Test [$testName] completed with [${errors.length}] errors. Press any key to continue.")
    }

    client.screen.readInput()
    client.stop()

    if(errors.nonEmpty) {
      throw new IllegalStateException("Errors encountered: [\n  " + errors.mkString("\n  ") + "\n].")
    }
  }
}
