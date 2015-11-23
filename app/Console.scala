import models.game.test.GameTest
import services.console.{ ConsoleTest, ConsoleGame }

object Console {
  private[this] val errorMsg = s"First argument must be one of [*, ${GameTest.all.map(_.testName).mkString(", ")}]."

  def main(args: Array[String]) {
    if (args.isEmpty) {
      new ConsoleGame()
    } else {
      args.headOption match {
        case Some(testName) => GameTest.fromString(testName) match {
          case Some(test) => ConsoleTest.run(test.newInstance(), args.contains("pause"))
          case None => if(Seq("*", "All", "all").contains(testName)) {
            allTests()
          } else {
            println(errorMsg) // scalastyle:ignore
          }
        }
        case None => println(errorMsg) // scalastyle:ignore
      }
    }
  }

  private[this] def allTests() = GameTest.all.foreach { provider =>
    val test = provider.newInstance()
    test.init()
    val messages = test.run()
    val e = test.getErrors
    def colorize(color: Int, s: String) = "[" + 27.toChar + "[" + color + "m" + s + 27.toChar + "[37m]"
    val status = if(e.isEmpty) { colorize(32, "OK") } else { colorize(31, "Error") }
    println(s"${provider.testName}: $status") // scalastyle:ignore
  }
}
