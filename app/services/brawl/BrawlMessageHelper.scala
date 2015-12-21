package services.brawl

import models._
import models.board.mutation.UpdateSegment
import models.player.Player
import utils.DateUtils

import scala.util.control.NonFatal

trait BrawlMessageHelper { this: BrawlService =>
  private[this] def incrementMoveCount(player: Player) = {
    val time = DateUtils.now
    if (firstMoveMade.isEmpty) {
      firstMoveMade = Some(time)
    }
    lastMoveMade = Some(time)
    player.board.incrementMoveCount(DateUtils.toMillis(time))
  }

  protected[this] def handleBrawlRequest(br: BrawlRequest) = {
    log.debug("Handling [" + br.message.getClass.getSimpleName.stripSuffix("$") + "] message from user [" + br.userId + "] for brawl [" + brawl.id + "].")
    try {
      val time = DateUtils.now
      brawlMessages += ((br.message, br.userId, time))
      val player = brawl.playersById(br.userId)

      br.message match {
        case x if brawl.completed.isDefined => log.warn(s"Received brawl message [${x.getClass.getSimpleName}] for completed brawl [$brawl.id].")
        case ActiveGemsLeft => player.activeGemsLeft().foreach { m =>
          incrementMoveCount(player)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active", Seq(m)))))
        }
        case ActiveGemsRight => player.activeGemsRight().foreach { m =>
          incrementMoveCount(player)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active", Seq(m)))))
        }
        case ActiveGemsClockwise => player.activeGemsClockwise().foreach { ms =>
          incrementMoveCount(player)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active", Seq(ms)))))
        }
        case ActiveGemsCounterClockwise => player.activeGemsCounterClockwise().foreach { ms =>
          incrementMoveCount(player)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active", Seq(ms)))))
        }
        case ActiveGemsStep => player.activeGemsStep().foreach { m =>
          incrementMoveCount(player)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active", Seq(m)))))
        }
        case ActiveGemsDrop =>
          incrementMoveCount(player)
          val messages = player.activeGemsDrop() +: player.board.fullTurn() :+ player.activeGemsCreate()
          sendToAll(PlayerUpdate(player.id, messages))
        case ResignBrawl =>
          log.info(s"Player [${player.id}] has resigned from brawl [$id].")
        case r =>
          log.warn(s"GameService received unknown brawl message [${r.getClass.getSimpleName.stripSuffix("$")}].")
      }
    } catch {
      case NonFatal(x) =>
        log.error(s"Exception processing brawl request [$br].", x)
        sender() ! ServerError(x.getClass.getSimpleName, x.getMessage)
    }
  }
}
