package services.brawl

import org.joda.time.Seconds
import utils.Logging
import utils.metrics.InstrumentedActor

trait BrawlHelper
    extends InstrumentedActor
    with Logging
    with ConnectionHelper
    with HistoryHelper
    with MessageHelper
    with TraceHelper
    with UpdateHelper { this: BrawlService =>

  protected[this] def elapsedSeconds = firstMoveMade.flatMap { first =>
    lastMoveMade.map { last =>
      Seconds.secondsBetween(first, last).getSeconds
    }
  }
}
