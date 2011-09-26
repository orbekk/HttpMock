package com.orbekk.logging

import java.util.logging

trait Logger {
  lazy val logger = logging.Logger.getLogger(this.getClass().getName())
}