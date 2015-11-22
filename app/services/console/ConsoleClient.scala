package services.console

import java.util.UUID

import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.{ TerminalPosition, TextCharacter, TextColor }
import models.game.Game
import models.game.test.TextGemPattern
import org.joda.time.LocalDateTime
import utils.{ DateUtils, Formatter }

class ConsoleClient(game: Game) {
  private[this] var activePlayer: Option[UUID] = None

  def getActivePlayer = activePlayer.map(x => game.playersById(x)).getOrElse(throw new IllegalStateException())
  def setActivePlayer(id: UUID) = {
    activePlayer = Some(id)
    addStatusLog(s"Player [$id] selected.")
  }

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

  private val rows = screen.getTerminalSize.getRows
  private val cols = screen.getTerminalSize.getColumns
  private val padding = (cols / 2) - game.players.map(_.board.width + 2).sum

  def stop() = screen.stopScreen()

  private[this] val statusLogs = collection.mutable.ListBuffer.empty[(LocalDateTime, String)]
  private[this] val statusRows = rows - 16
  private[this] def writeStatus() = {
    graphics.setForegroundColor(TextColor.ANSI.WHITE)
    val logs = statusLogs.zipWithIndex.takeRight(statusRows).reverse
    for (l <- logs.zipWithIndex) {
      val logIndex = l._1._2
      val rowIndex = l._2
      val log = l._1._1
      val msg = "[" + Formatter.niceTime(log._1.toLocalTime) + "] (" + (logIndex + 1) + "/" + statusLogs.size + "): " + log._2
      graphics.putString(0, rows - rowIndex - 1, msg + (0 until cols).map(x => " ").mkString(""))
    }
    render()
  }

  def addStatusLog(s: String) = {
    statusLogs += (DateUtils.now -> s)
    writeStatus()
  }

  def render() = {
    if (game.players.isEmpty) {
      throw new IllegalStateException("No players in client.")
    }

    game.players.zipWithIndex.foreach { p =>
      val fg = if (activePlayer.contains(p._1.id)) { TextColor.ANSI.CYAN } else { TextColor.ANSI.WHITE }
      val xOffset = padding + (p._2 * 16) + 2
      ConsoleBorders.render(this, xOffset, 1, p._1, fg, TextColor.ANSI.BLACK)
      (0 until p._1.board.height).foreach { y =>
        (0 until p._1.board.width).foreach { x =>
          val pattern = TextGemPattern.pair(p._1.board, p._1.board.at(x, y), x, y)
          val targetX = xOffset + 1 + (x * 2)
          val targetY = p._1.board.height - y + 1
          screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, TextColor.ANSI.BLACK))
          screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, TextColor.ANSI.BLACK))
        }
      }
      p._1.activeGems.foreach { activeGem =>
        val pattern = TextGemPattern.pair(p._1.board, Some(activeGem.gem), activeGem.x, activeGem.y)
        val targetX = xOffset + 1 + (activeGem.x * 2)
        val targetY = p._1.board.height - activeGem.y + 1
        screen.setCharacter(targetX, targetY, new TextCharacter(pattern._1, pattern._3, TextColor.ANSI.BLACK))
        screen.setCharacter(targetX + 1, targetY, new TextCharacter(pattern._2, pattern._3, TextColor.ANSI.BLACK))
      }
    }

    screen.setCursorPosition(new TerminalPosition(cols - 1, rows - 1))
    screen.refresh()
  }
}
