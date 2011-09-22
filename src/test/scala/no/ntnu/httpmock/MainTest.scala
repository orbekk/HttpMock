package no.ntnu.httpmock

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class MainTest extends FunSuite {
  test("A dummy test that should pass") {
    assert(true === true)
  }
}