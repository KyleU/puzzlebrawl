package services.console

import models.test.brawl.BrawlTest

object ConsoleTest {
  def run(test: BrawlTest, pauseBeforeRun: Boolean) = {
    val testName = utils.Formatter.className(test).replaceAllLiterally("Test", "")
    test.init()
    test.cloneOriginal()

    val client = new ConsoleClient(test.brawl)

    if (pauseBeforeRun) {
      client.addStatusLog(s"BrawlTest [$testName] initialized.")
      client.screen.readInput()
    }
    client.render()

    test.run()
    client.render()

    val errors = test.getErrors
    if (errors.isEmpty) {
      client.addStatusLog(s"BrawlTest [$testName] completed successfully. Press any key to continue.")
    } else {
      client.addStatusLog(s"BrawlTest [$testName] completed with [${errors.length}] errors. Press any key to continue.")
    }

    client.screen.readInput()
    client.stop()

    if (errors.nonEmpty) {
      throw new IllegalStateException("Errors encountered: [\n  " + errors.mkString("\n  ") + "\n].")
    }
  }
}
