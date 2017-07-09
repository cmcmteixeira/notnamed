package com.notnamed.commons.macros

import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

import scala.reflect.macros.{Universe, blackbox}
import scala.reflect.runtime.currentMirror
import scala.reflect.api.Quasiquotes


class GenericCRUDRoutesSpec extends FlatSpec with MockitoSugar {

  class Fixture {
    val context = mock[blackbox.Context]
    val universe = mock[Universe]
  }
}
