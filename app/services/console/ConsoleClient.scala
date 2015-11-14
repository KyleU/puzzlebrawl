package services.console

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.{ TerminalPosition, TextCharacter, TextColor }
import models.game.board.Board
import models.game.gem.GemStream
import org.joda.time.LocalDateTime
import utils.{ DateUtils, Formatter }

class ConsoleClient {
  var boards = Seq.empty[(Board, Int, Int)]

  val gemStream = GemStream(0)

  val screen = {
    val factory = new DefaultTerminalFactory()
    factory.setSuppressSwingTerminalFrame(true)
    val terminal = factory.createTerminal()
    val s = new TerminalScreen(terminal)
    s.startScreen()
    s.clear()
    s
  }

  val graphics = screen.newTextGraphics()

  val rows = screen.getTerminalSize.getRows
  val cols = screen.getTerminalSize.getColumns

  def stop() = screen.stopScreen()

  private var nextBoardX = 0
  private var nextBoardY = 0

  def add(b: Board) = {
    if(nextBoardX + (b.width * 2) + 3 > cols) {
      nextBoardX = 0
      nextBoardY += boards.filter(_._3 == nextBoardY).map(_._1.height + 3).max
    }
    boards = boards :+ ((b, nextBoardX, nextBoardY))
    nextBoardX += (b.width * 2) + 3
  }

  private[this] val statusLogs = collection.mutable.ListBuffer.empty[(LocalDateTime, String)]
  private[this] var statusIndex = -1
  private[this] def writeStatus() = {
    val log = statusLogs(statusIndex)
    val msg = "[" + Formatter.niceTime(log._1.toLocalTime) + "] (" + (statusIndex + 1) + "/" + statusLogs.size + "): " + log._2
    graphics.putString(0, rows - 1, msg + (0 until cols).map(x => " ").mkString(""))
    render()
  }

  def addStatusLog(s: String) = {
    statusLogs += (DateUtils.now -> s)
    statusIndex = statusLogs.length - 1
    writeStatus()
  }

  def previousStatus() = if(statusIndex > 0) {
    statusIndex -= 1
    writeStatus()
  }

  def nextStatus() = if(statusIndex < statusLogs.size - 1) {
    statusIndex += 1
    writeStatus()
  }

  def render() = {
    if(boards.isEmpty) {
      throw new IllegalStateException("No boards in client.")
    }

    boards.foreach { b =>
      ConsoleBorders.render(this, b._2, b._3, b._1.width, b._1.height, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)
      (0 until b._1.width).foreach { x =>
        (0 until b._1.height).foreach { y =>
          val pattern = ConsoleGemPattern.pattern(b._1.at(x, y))
          val targetX = b._2 + 1 + (x * 2)
          val targetY = b._3 + 1 + (b._1.height - y - 1)

          screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, TextColor.ANSI.BLACK))
          screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, TextColor.ANSI.BLACK))
        }
      }
    }

    screen.setCursorPosition(new TerminalPosition(cols - 1, rows - 1))
    screen.refresh()
  }
}
