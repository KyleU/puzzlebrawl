package models.test.service

import akka.testkit.TestProbe
import models.{ VersionResponse, GetVersion, Pong, Ping }
import models.user.User
import services.connection.ConnectionService
import utils.{ ApplicationContext, DateUtils, Logging }

object ConnectionServiceTest extends Logging {
  lazy val ctx = play.api.Play.current.injector.instanceOf(classOf[ApplicationContext])

  def testConnection() = {
    implicit val system = play.api.Play.current.actorSystem
    val testProbe = TestProbe()

    val initStart = DateUtils.nowMillis
    val conn = system.actorOf(ConnectionService.props(None, ctx.supervisor, User.mock, testProbe.ref, "127.0.0.1"))
    val initMs = DateUtils.nowMillis - initStart

    val runStart = DateUtils.nowMillis

    conn ! Ping(DateUtils.nowMillis)
    testProbe.expectMsgClass(classOf[Pong])

    conn ! GetVersion
    testProbe.expectMsgClass(classOf[VersionResponse])

    val runMs = DateUtils.nowMillis - runStart

    "Ok"
  }
}
