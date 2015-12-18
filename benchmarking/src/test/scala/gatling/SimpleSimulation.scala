package gatling

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SimpleSimulation extends Simulation {
  val domain = "puzzlebrawl.dev"
  val wsUrl = s"ws://$domain/websocket"
  val pauseSeconds = 1

  val httpConf = http
    .baseURL(s"http://$domain")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val homeRequest = http("Splash Page").get("/").check(status.is(200))
  val friendsRequest = http("Home Page").get("/friends").check(status.is(200))
  val brawlRequest = http("Brawl Page").get("/play").check(status.is(200))

  val wsOpen = ws("Open Websocket").open(wsUrl)
  val wsStartBrawl = ws("StartBrawl Message").sendText(jsonFor("StartBrawl", """{ "scenario": "normal" }""")).check(wsAwait.within(2.seconds).expect(1))
  val wsActiveLeft = ws("ActiveGemsLeft Message").sendText(jsonFor("ActiveGemsLeft", "{}")).check(wsAwait.within(2.seconds).expect(1))
  val wsActiveRight = ws("ActiveGemsRight Message").sendText(jsonFor("ActiveGemsRight", "{}")).check(wsAwait.within(2.seconds).expect(1))
  val wsActiveDrop = ws("ActiveGemsDrop Message").sendText(jsonFor("ActiveGemsDrop", "{}")).check(wsAwait.within(2.seconds).expect(1))
  val wsClose = ws("Close Websocket").close

  val scn = scenario("Basic Test")
    .exec(homeRequest).pause(pauseSeconds)
    .exec(friendsRequest).pause(pauseSeconds)
    .exec(brawlRequest).pause(pauseSeconds)
    .exec(wsOpen).pause(pauseSeconds)
    .exec(wsStartBrawl).pause(pauseSeconds)
    .repeat(5, "i") {
      exec(wsActiveLeft).pause(pauseSeconds)
      .exec(wsActiveRight).pause(pauseSeconds)
      .exec(wsActiveDrop).pause(pauseSeconds)
    }
    .exec(wsClose)

  def jsonFor(c: String, v: String) = s"""{ "c": "$c", "v": $v }"""

  setUp(scn.inject(
    rampUsers(100) over 10.seconds
  ).protocols(httpConf))
}
