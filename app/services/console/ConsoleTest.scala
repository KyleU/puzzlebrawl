package services.console

import models.test.brawl.Test

object ConsoleTest {
  def run(test: Test, pauseBeforeRun: Boolean) = {
    val testName = test.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("Test", "")
    test.init()

    test.test.board.cloneTo(test.original.board)

    val client = new ConsoleClient(test.game)

    if (pauseBeforeRun) {
      client.addStatusLog(s"Test [$testName] initialized.")
      client.screen.readInput()
    }
    client.render()

    test.run()
    client.render()

    val errors = test.getErrors
    if (errors.isEmpty) {
      client.addStatusLog(s"Test [$testName] completed successfully. Press any key to continue.")
    } else {
      client.addStatusLog(s"Test [$testName] completed with [${errors.length}] errors. Press any key to continue.")
    }

    client.screen.readInput()
    client.stop()

    if (errors.nonEmpty) {
      throw new IllegalStateException("Errors encountered: [\n  " + errors.mkString("\n  ") + "\n].")
    }
  }
}
