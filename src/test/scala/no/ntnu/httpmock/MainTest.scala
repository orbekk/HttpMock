package no.ntnu.httpmock

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class MainTest extends FunSuite {
  test("Fib should return 1 as a base case") {
    assert(Main.fib(0) === 1)
    assert(Main.fib(1) === 1)
  }

  test("Fib should work for some numbers") {
    assert(Main.fib(2) === 2)
    assert(Main.fib(3) === 3)
    assert(Main.fib(4) === 5)
    assert(Main.fib(5) === 8)
  }
}