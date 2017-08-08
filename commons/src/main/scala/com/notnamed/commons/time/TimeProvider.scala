package com.notnamed.commons.time


trait TimeProvider {
  def now() : Long
}


object TimeProvider extends TimeProvider{
  def now() : Long = System.currentTimeMillis()
}