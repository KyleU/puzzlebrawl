package services.sandbox

import models.game.Game

import scala.concurrent.Future

object Scratchpad {
  def run() = {
    val game = Game.blank()
    val ret = "Ok!"
    Future.successful(ret)
  }
}
