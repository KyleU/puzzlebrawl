package services.brawl

import models._
import models.board.mutation.Mutation.TargetChanged
import models.board.mutation.{ Mutation, UpdateSegment }

import scala.util.control.NonFatal

trait BrawlMessageHelper { this: BrawlService =>
  protected[this] def handleBrawlRequest(br: BrawlRequest) = {
    log.debug("Handling [" + utils.Formatter.className(br.message) + "] message from user [" + br.userId + "] for brawl [" + brawl.id + "].")
    val time = utils.DateUtils.now
    try {
      logBrawlMessageReceive(br.message, br.userId, time)
      val player = brawl.playersById(br.userId)

      def sendMove(m: Mutation, key: String = "active-move") = sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment(key, Seq(m)))))

      br.message match {
        case x if brawl.completed.isDefined => log.warn(s"Received brawl message [${x.getClass.getSimpleName}] for completed brawl [$brawl.id].")
        case ActiveGemsLeft => player.activeGemsLeft().foreach(m => sendMove(m))
        case ActiveGemsRight => player.activeGemsRight().foreach(m => sendMove(m))
        case ActiveGemsClockwise => player.activeGemsClockwise().foreach(m => sendMove(m))
        case ActiveGemsCounterClockwise => player.activeGemsCounterClockwise().foreach(m => sendMove(m))
        case ActiveGemsStep => player.activeGemsStep().foreach(m => sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("active-step", Seq(m))))))
        case ActiveGemsDrop => sendToAll(PlayerUpdate(player.id, player.dropActiveFullTurn(brawl)))

        case st: SelectTarget => if (!player.target.contains(st.target)) {
          player.target = Some(st.target)
          sendToAll(PlayerUpdate(player.id, Seq(UpdateSegment("target", Seq(TargetChanged(st.target))))))
        }

        case ResignBrawl => log.info(s"Player [${player.id}] has resigned from brawl [$id].")

        case r => log.warn(s"BrawlService received unknown brawl message [${utils.Formatter.className(r)}].")
      }
    } catch {
      case NonFatal(x) =>
        log.error(s"Exception processing brawl request [$br].", x)
        sender() ! ServerError(x.getClass.getSimpleName, x.getMessage)
    }
    logBrawlMessageComplete(br.message, br.userId, time)
  }
}
