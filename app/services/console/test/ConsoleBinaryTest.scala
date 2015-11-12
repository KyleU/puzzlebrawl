package services.console.test

import models.game.board.Board

abstract class ConsoleBinaryTest(width: Int, height: Int) extends ConsoleBaseTest {
  val board = Board(width, height)
  val test = Board(width, height)

  def run(): Unit

  def main(args: Array[String]) {
    val pauseBeforeRun = args.length > 0 && args(0) == "pause"

    client.add(board)
    client.add(test)

    init()

    if(pauseBeforeRun) {
      client.addStatusLog(s"Test [$testName] initialized.")
      client.screen.readInput()
    }
    run()
    client.render()

    val errors = (0 until width).flatMap { x =>
      (0 until height).flatMap { y =>
        val testVal = test.at(x, y)
        val observedVal = board.at(x, y)
        if(observedVal == testVal) {
          None
        } else {
          Some((testVal, observedVal, x, y))
        }
      }
    }

    if(errors.isEmpty) {
      client.addStatusLog(s"Test [$testName] completed successfully. Press any key to continue.")
    } else {
      client.addStatusLog(s"Test [$testName] completed with [${errors.length}] errors. Press any key to continue.")
    }

    client.screen.readInput()
    client.stop()

    if(errors.nonEmpty) {
      val errorStrings = errors.map(x => s"${x._1} is not equal to ${x._2} at position [${x._3}, ${x._4}]")
      throw new IllegalStateException("Errors encountered: [\n  " + errorStrings.mkString("\n  ") + "\n].")
    }
  }
}
