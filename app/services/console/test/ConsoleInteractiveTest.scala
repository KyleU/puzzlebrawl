package services.console.test

import com.googlecode.lanterna.input.{ KeyStroke, KeyType }
import services.console.ConsoleClient

import scala.annotation.tailrec

abstract class ConsoleInteractiveTest extends ConsoleBaseTest {
  def inputCharacter(keyStroke: KeyStroke): Boolean

  @tailrec
  private def processInput(input: KeyStroke, client: ConsoleClient): Unit = {
    val ret = input.getKeyType match {
      case KeyType.Character => input.getCharacter match {
        case x if x == 'q' || (x == 'd' && input.isCtrlDown) => false
        case _ => inputCharacter(input)
      }
      case KeyType.Escape => false
      case KeyType.ArrowUp => client.previousStatus(); true
      case KeyType.ArrowDown => client.nextStatus(); true
      case _ => inputCharacter(input)
    }
    if (ret) {
      processInput(client.screen.readInput(), client)
    }
  }

  def main(args: Array[String]) {
    init()
    client.addStatusLog(s"Test [$testName] initialized.")
    processInput(client.screen.readInput(), client)
    client.stop()
  }
}
