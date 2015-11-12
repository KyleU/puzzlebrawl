package services.console.test

import java.util.Random

import services.console.ConsoleClient

abstract class ConsoleBaseTest() {
  val testName = this.getClass.getSimpleName.stripSuffix("$").replaceAllLiterally("ConsoleTest", "")
  protected val r = new Random()
  protected val client = new ConsoleClient()

  def init(): Unit
}
