package no.ntnu.httpmock

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import Types.{MockRequest, MockResponse}
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class SimpleMockHandlerTest extends FunSuite {
  val handler = new SimpleMockHandler
  val exampleMockParameters0 = ListMap("Fruits" -> Array("Apple", "Orange"))
  val exampleRequestParameters0 = ListMap("Animals" -> Array("Cat")) ++
      exampleMockParameters0

  def initExampleHandler1() = {
//    handler.registerMock(MockRequest("Boo"), MockResponse("Moo"))
  }

  test("MockHandler should return None on unknown request") {
//    initExampleHandler1()
//    assert(handler.getResponseFor(MockRequest("Zoo")) isEmpty)
  }

  test("MockHandler should return the response for a known request") {
//    initExampleHandler1()
//    assert(handler.getResponseFor(MockRequest("Boo")) === Some(MockResponse("Moo")))
  }
}