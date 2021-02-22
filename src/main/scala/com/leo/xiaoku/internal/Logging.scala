package com.leo.xiaoku.internal

import org.slf4j.{Logger, LoggerFactory}

trait Logging {


  private var log_ : Logger = null

  protected def logName: String = {
    this.getClass.getName
  }

  protected def log: Logger = {
    if (log_ == null) {
      log_ = LoggerFactory.getLogger(logName)
    }
    log_
  }

  protected def logInfo(msg: String): Unit = {
    if (log.isInfoEnabled) log.info(msg)
  }

}
