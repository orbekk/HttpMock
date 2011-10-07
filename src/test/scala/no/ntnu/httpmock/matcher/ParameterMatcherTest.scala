package no.ntnu.httpmock.matcher

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class ParameterMatcherTest extends FunSuite {
  val exampleMockParameters0 = ListMap("Fruits" -> List("Apple", "Orange"))
  val exampleMockParameters1 = ListMap("Animals" -> List("Cat")) ++
      exampleMockParameters0

  test("internalMatches matches with equal properties") {
     val matcher = new ParameterMatcher(Map.empty, ParameterMatcher.defaultOptions)
     assert(matcher.internalMatches(exampleMockParameters0, exampleMockParameters0))
     assert(matcher.internalMatches(exampleMockParameters1, exampleMockParameters1))
  }

  test("internalMatches does not match with different properties") {
     val matcher = new ParameterMatcher(Map.empty, ParameterMatcher.defaultOptions)
     assert(!matcher.internalMatches(exampleMockParameters0, exampleMockParameters1))
     assert(!matcher.internalMatches(exampleMockParameters1, exampleMockParameters0))
  }

  test("internalMatches ignores properties correctly") {
     val matcher = new ParameterMatcher(Map.empty,
         ParameterMatcher.Options(List("Animals")))
     assert(matcher.internalMatches(exampleMockParameters0, exampleMockParameters1))
     assert(matcher.internalMatches(exampleMockParameters1, exampleMockParameters0))
  }
}
