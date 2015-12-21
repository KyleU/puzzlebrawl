package services.supervisor

import utils.Logging
import utils.metrics.InstrumentedActor

trait ActorSupervisorHelper extends InstrumentedActor with Logging with ActorSupervisorBrawlHelper { this: ActorSupervisor =>

}
