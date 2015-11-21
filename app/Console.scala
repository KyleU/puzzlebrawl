import models.game.test.GameTest
import services.console.{ ConsoleTest, ConsoleGame }

object Console {
  def main(args: Array[String]) {
    if (args.isEmpty) {
      new ConsoleGame()
    } else {
      args.headOption.flatMap(GameTest.fromString) match {
        case Some(test) => ConsoleTest.run(test.newInstance(), args.contains("pause"))
        case x =>
          val msg = s"First argument must be one of [${GameTest.all.map(_.testName).mkString(", ")}]."
          println(msg) // scalastyle:ignore
      }
    }
  }
}
