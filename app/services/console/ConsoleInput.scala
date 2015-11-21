package services.console

import com.googlecode.lanterna.input.{ KeyStroke, KeyType }

import scala.annotation.tailrec

abstract class ConsoleInput {
  def inputCharacter(keyStroke: KeyStroke): Boolean

  def startInputLoop(client: ConsoleClient) = processInput(client.screen.readInput(), client)

  @tailrec
  private def processInput(input: KeyStroke, client: ConsoleClient): Unit = {
    val ret = input.getKeyType match {
      case KeyType.Character => input.getCharacter match {
        case x if x.charValue == 'q' || (x.charValue == 'd' && input.isCtrlDown) => false
        case _ => inputCharacter(input)
      }
      case KeyType.Escape => false
      case _ => inputCharacter(input)
    }
    if (ret) {
      processInput(client.screen.readInput(), client)
    }
  }
}
