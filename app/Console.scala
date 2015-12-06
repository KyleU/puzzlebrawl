import java.util.UUID

import models.test.brawl.BrawlTest
import services.console.{ ConsoleTest, ConsoleGame }

object Console {
  private[this] val errorMsg = s"First argument must be one of [*, ${BrawlTest.all.map(_.testName).mkString(", ")}]."

  def main(args: Array[String]) {
    if (args.isEmpty) {
      new ConsoleGame()
    } else {
      args.headOption match {
        case Some(testName) => BrawlTest.fromString(testName) match {
          case Some(test) => ConsoleTest.run(test.newInstance(UUID.randomUUID), args.contains("pause"))
          case None => if (Seq("*", "All", "all").contains(testName)) {
            allTests()
          } else {
            println(errorMsg) // scalastyle:ignore
          }
        }
        case None => println(errorMsg) // scalastyle:ignore
      }
    }
  }

  private[this] def allTests() = BrawlTest.all.foreach { provider =>
    val test = provider.newInstance(UUID.randomUUID)
    test.init()
    test.cloneOriginal()
    val messages = test.run()
    val e = test.getErrors
    def colorize(color: Int, s: String) = "[" + 27.toChar + "[" + color + "m" + s + 27.toChar + "[37m]"
    val status = if (e.isEmpty) { colorize(32, "OK") } else { colorize(31, "Error") }
    println(s"${provider.testName}: $status") // scalastyle:ignore
  }
}
