package services.audit

import models.brawl.Brawl
import utils.Config

@javax.inject.Singleton
class GameAuditService @javax.inject.Inject() (cfg: Config) {
  private[this] val rootDir = new java.io.File(cfg.fileCacheDir)
  if (!rootDir.exists() || !rootDir.isDirectory) {
    throw new IllegalStateException("Cache directory [" + cfg.fileCacheDir + "] does not exist.")
  }

  def audit(brawl: Brawl) = {

  }
}
