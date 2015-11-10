package services.console

import java.util.Random

import com.googlecode.lanterna.input.KeyStroke

import scala.annotation.tailrec

abstract class ConsoleTest {
  protected val r = new Random()
  protected val client = new ConsoleClient()

  def init(): Unit
  def inputCharacter(keyStroke: KeyStroke): Boolean

  @tailrec
  private def processInput(input: KeyStroke, client: ConsoleClient): Unit = {
    val ret = input.getCharacter match {
      case x if x == 'q' || x == '\n' || (x == 'd' && input.isCtrlDown) => false
      case _ => inputCharacter(input)
    }
    if (ret) {
      processInput(client.screen.readInput(), client)
    }
  }

  def main(args: Array[String]) {
    init()
    client.render()
    processInput(client.screen.readInput(), client)
    client.stop()
  }
}
