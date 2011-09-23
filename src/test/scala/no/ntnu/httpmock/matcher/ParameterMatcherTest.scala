package no.ntnu.httpmock.matcher

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class ParameterMatcherTest extends FunSuite {
  val exampleMockParameters0 = ListMap("Fruits" -> Array("Apple", "Orange"))
  val exampleRequestParameters0 = ListMap("Animals" -> Array("Cat")) ++
      exampleMockParameters0

  test("internalMatches matches with equal properties") {
     val matcher = new ParameterMatcher(Map.empty)
     assert(matcher.internalMatches(exampleMockParameters0, exampleMockParameters0))
  }
}