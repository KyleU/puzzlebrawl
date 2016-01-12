package models.test.service

import java.util.UUID

import akka.testkit.TestProbe
import models.user.{ PlayerRecord, User }
import models._
import services.brawl.BrawlService
import services.connection.ConnectionService
import utils.{ ApplicationContext, DateUtils, Logging }

object BrawlServiceTest extends Logging {
  lazy val ctx = play.api.Play.current.injector.instanceOf(classOf[ApplicationContext])

  def testBrawl() = {
    implicit val system = play.api.Play.current.actorSystem
    val testProbe = TestProbe()

    val initStart = DateUtils.nowMillis
    val connId = UUID.randomUUID
    val conn = system.actorOf(ConnectionService.props(Some(connId), ctx.supervisor, User.mock, testProbe.ref))
    val playerRecords = Seq(PlayerRecord(User.mock.id, "Test User", Some(connId), Some(conn)))

    system.actorOf(BrawlService.props(UUID.randomUUID, "normal", playerRecords, 0, s => {}))

    testProbe.expectMsgClass(classOf[BrawlJoined])

    val runStart = DateUtils.nowMillis

    conn ! ActiveGemsLeft
    val update1 = testProbe.expectMsgClass(classOf[PlayerUpdate])
    assert(update1.segments.size == 1)

    conn ! ActiveGemsRight
    val update2 = testProbe.expectMsgClass(classOf[PlayerUpdate])
    assert(update2.segments.size == 1)

    conn ! ActiveGemsDrop
    testProbe.expectMsgClass(classOf[PlayerUpdate])

    "Ok!"
  }
}
