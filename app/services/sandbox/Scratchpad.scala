package services.sandbox

import java.awt.geom._
import java.awt.image.BufferedImage
import java.awt.{ Graphics2D, BasicStroke, Color }

import scala.concurrent.Future

object Scratchpad {
  val mult = 4.0 // 128
  val inset = 4 // 16
  val lineWidth = 1.0f

  def run() = {
    writeGemImage()
    val ret = "Ok!"
    Future.successful(ret)
  }

  private[this] def writeGemImage() = {
    val canvas = new BufferedImage(32 * mult.toInt * 16, 32 * mult.toInt * 5, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()

    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    g.setStroke(new BasicStroke(lineWidth))
    g.setColor(Color.WHITE)

    def forEachColor(f: (Graphics2D, Int) => Unit) = (0 until 5).foreach(i => f(g, i * 32))

    forEachColor((g, x) => fullGem(g, 0, x))
    forEachColor((g, x) => fullGem(g, 32, x))
    forEachColor((g, x) => fullGem(g, 64, x))
    forEachColor((g, x) => fullGem(g, 96, x))
    forEachColor((g, x) => fullGem(g, 128, x))
    forEachColor((g, x) => fullGem(g, 160, x))

    forEachColor((g, x) => topGem(g, 192, x))
    forEachColor((g, x) => middleGem(g, 288, x))
    forEachColor((g, x) => bottomGem(g, 384, x))

    forEachColor((g, x) => crashGem(g, 480, x))

    g.dispose()

    javax.imageio.ImageIO.write(canvas, "png", new java.io.File("test.png"))
  }

  private[this] def line(g: Graphics2D, x1: Int, y1: Int, x2: Int, y2: Int, xOffset: Int, yOffset: Int) = {
    g.draw(new Line2D.Double((xOffset + x1) * mult, (yOffset + y1) * mult, (xOffset + x2) * mult, (yOffset + y2) * mult))
  }

  private[this] def fullGem(g: Graphics2D, xOffset: Int, yOffset: Int) = {
    line(g, 4, 1, 28, 1, xOffset, yOffset) // Top
    line(g, 4 + inset, 1 + inset, 28 - inset, 1 + inset, xOffset, yOffset)

    line(g, 31, 4, 31, 28, xOffset, yOffset) // Right
    line(g, 31 - inset, 4 + inset, 31 - inset, 28 - inset, xOffset, yOffset)

    line(g, 4, 31, 28, 31, xOffset, yOffset) // Bottom
    line(g, 4 + inset, 31 - inset, 28 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 4, 1, 28, xOffset, yOffset) // Left
    line(g, 1 + inset, 4 + inset, 1 + inset, 28 - inset, xOffset, yOffset)

    line(g, 28, 1, 31, 4, xOffset, yOffset) // UR
    line(g, 28, 1, 28 - inset, 1 + inset, xOffset, yOffset)
    line(g, 31, 4, 31 - inset, 4 + inset, xOffset, yOffset)
    line(g, 28 - inset, 1 + inset, 31 - inset, 4 + inset, xOffset, yOffset) // UR

    line(g, 31, 28, 28, 31, xOffset, yOffset) // BR
    line(g, 31, 28, 31 - inset, 28 - inset, xOffset, yOffset)
    line(g, 28, 31, 28 - inset, 31 - inset, xOffset, yOffset)
    line(g, 31 - inset, 28 - inset, 28 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 28, 4, 31, xOffset, yOffset) // BL
    line(g, 1, 28, 1 + inset, 28 - inset, xOffset, yOffset)
    line(g, 4, 31, 4 + inset, 31 - inset, xOffset, yOffset)
    line(g, 1 + inset, 28 - inset, 4 + inset, 31 - inset, xOffset, yOffset)

    line(g, 4, 1, 1, 4, xOffset, yOffset) // UL
    line(g, 4, 1, 4 + inset, 1 + inset, xOffset, yOffset)
    line(g, 1, 4, 1 + inset, 4 + inset, xOffset, yOffset)
    line(g, 4 + inset, 1 + inset, 1 + inset, 4 + inset, xOffset, yOffset)
  }

  private[this] def topGem(g: Graphics2D, xOffset: Int, yOffset: Int) = {
    line(g, 4, 1, 92, 1, xOffset, yOffset) // Top
    line(g, 4 + inset, 1 + inset, 92 - inset, 1 + inset, xOffset, yOffset)

    line(g, 95, 4, 95, 32, xOffset, yOffset) // Right
    line(g, 95 - inset, 4 + inset, 95 - inset, 32, xOffset, yOffset)

    line(g, 1, 4, 1, 32, xOffset, yOffset) // Left
    line(g, 1 + inset, 4 + inset, 1 + inset, 32, xOffset, yOffset)

    line(g, 92, 1, 95, 4, xOffset, yOffset) // UR
    line(g, 92, 1, 92 - inset, 1 + inset, xOffset, yOffset)
    line(g, 95, 4, 95 - inset, 4 + inset, xOffset, yOffset)
    line(g, 92 - inset, 1 + inset, 95 - inset, 4 + inset, xOffset, yOffset) // UR

    line(g, 4, 1, 1, 4, xOffset, yOffset) // UL
    line(g, 4, 1, 4 + inset, 1 + inset, xOffset, yOffset)
    line(g, 1, 4, 1 + inset, 4 + inset, xOffset, yOffset)
    line(g, 4 + inset, 1 + inset, 1 + inset, 4 + inset, xOffset, yOffset)
  }

  private[this] def middleGem(g: Graphics2D, xOffset: Int, yOffset: Int) = {
    line(g, 95, 0, 95, 32, xOffset, yOffset) // Right
    line(g, 95 - inset, 0, 95 - inset, 32, xOffset, yOffset)

    line(g, 1, 0, 1, 32, xOffset, yOffset) // Left
    line(g, 1 + inset, 0, 1 + inset, 32, xOffset, yOffset)
  }

  private[this] def bottomGem(g: Graphics2D, xOffset: Int, yOffset: Int) = {
    line(g, 95, 0, 95, 28, xOffset, yOffset) // Right
    line(g, 95 - inset, 0, 95 - inset, 28 - inset, xOffset, yOffset)

    line(g, 4, 31, 92, 31, xOffset, yOffset) // Bottom
    line(g, 4 + inset, 31 - inset, 92 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 0, 1, 28, xOffset, yOffset) // Left
    line(g, 1 + inset, 0, 1 + inset, 28 - inset, xOffset, yOffset)

    line(g, 95, 28, 92, 31, xOffset, yOffset) // BR
    line(g, 95, 28, 95 - inset, 28 - inset, xOffset, yOffset)
    line(g, 92, 31, 92 - inset, 31 - inset, xOffset, yOffset)
    line(g, 95 - inset, 28 - inset, 92 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 28, 4, 31, xOffset, yOffset) // BL
    line(g, 1, 28, 1 + inset, 28 - inset, xOffset, yOffset)
    line(g, 4, 31, 4 + inset, 31 - inset, xOffset, yOffset)
    line(g, 1 + inset, 28 - inset, 4 + inset, 31 - inset, xOffset, yOffset)
  }

  private[this] def crashGem(g: Graphics2D, xOffset: Int, yOffset: Int) = {
    line(g, 12, 1, 20, 1, xOffset, yOffset) // Top
    line(g, 12 + inset, 1 + inset, 20 - inset, 1 + inset, xOffset, yOffset)

    line(g, 31, 12, 31, 20, xOffset, yOffset) // Right
    line(g, 31 - inset, 12 + inset, 31 - inset, 20 - inset, xOffset, yOffset)

    line(g, 12, 31, 20, 31, xOffset, yOffset) // Bottom
    line(g, 12 + inset, 31 - inset, 20 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 12, 1, 20, xOffset, yOffset) // Left
    line(g, 1 + inset, 12 + inset, 1 + inset, 20 - inset, xOffset, yOffset)

    line(g, 20, 1, 31, 12, xOffset, yOffset) // UR
    line(g, 20, 1, 20 - inset, 1 + inset, xOffset, yOffset)
    line(g, 31, 12, 31 - inset, 12 + inset, xOffset, yOffset)
    line(g, 20 - inset, 1 + inset, 31 - inset, 12 + inset, xOffset, yOffset) // UR

    line(g, 31, 20, 20, 31, xOffset, yOffset) // BR
    line(g, 31, 20, 31 - inset, 20 - inset, xOffset, yOffset)
    line(g, 20, 31, 20 - inset, 31 - inset, xOffset, yOffset)
    line(g, 31 - inset, 20 - inset, 20 - inset, 31 - inset, xOffset, yOffset)

    line(g, 1, 20, 12, 31, xOffset, yOffset) // BL
    line(g, 1, 20, 1 + inset, 20 - inset, xOffset, yOffset)
    line(g, 12, 31, 12 + inset, 31 - inset, xOffset, yOffset)
    line(g, 1 + inset, 20 - inset, 12 + inset, 31 - inset, xOffset, yOffset)

    line(g, 12, 1, 1, 12, xOffset, yOffset) // UL
    line(g, 12, 1, 12 + inset, 1 + inset, xOffset, yOffset)
    line(g, 1, 12, 1 + inset, 12 + inset, xOffset, yOffset)
    line(g, 12 + inset, 1 + inset, 1 + inset, 12 + inset, xOffset, yOffset)
  }
}
