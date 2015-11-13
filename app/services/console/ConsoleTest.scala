package services.console

import models.game.test.GameTest

object ConsoleTest {
  def main(args: Array[String]) {
    args.headOption.flatMap(GameTest.fromString) match {
      case Some(test) => run(test)
      case x => throw new IllegalStateException(s"First argument must be one of [${GameTest.all.map(_.testName).mkString(", ")}].")
    }
  }

  def run(test: GameTest) = {
    test.init()
    val client = new ConsoleClient()
    client.add(test.board)
    client.add(test.testBoard)

    val pauseBeforeRun = true // ??
    if(pauseBeforeRun) {
      client.addStatusLog(s"Test [${test.testName}] initialized.")
      client.screen.readInput()
    }
    client.render()

    test.run()
    client.render()

    val errors = (0 until test.width).flatMap { x =>
      (0 until test.height).flatMap { y =>
        val testVal = test.testBoard.at(x, y)
        val observedVal = test.board.at(x, y)
        if(observedVal == testVal) {
          None
        } else {
          Some((testVal, observedVal, x, y))
        }
      }
    }

    if(errors.isEmpty) {
      client.addStatusLog(s"Test [${test.testName}] completed successfully. Press any key to continue.")
    } else {
      client.addStatusLog(s"Test [${test.testName}] completed with [${errors.length}] errors. Press any key to continue.")
    }

    client.screen.readInput()
    client.stop()

    if(errors.nonEmpty) {
      val errorStrings = errors.map(x => s"[${x._3}, ${x._4}]: ${x._1.getOrElse("Empty")} is not equal to ${x._2.getOrElse("Empty")}.")
      throw new IllegalStateException("Errors encountered: [\n  " + errorStrings.mkString("\n  ") + "\n].")
    }
  }
}
