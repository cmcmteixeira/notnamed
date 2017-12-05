package com.notnamed.commons.time

import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}




class TimeProvider(clock: Clock){
  def now() : Timestamp = Timestamp.valueOf(LocalDateTime.now(clock))
}