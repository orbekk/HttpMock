package no.ntnu.httpmock

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap
import scala.collection.JavaConversions.mapAsJavaMap
import no.ntnu.httpmock.matcher.ParameterMatcher
import javax.servlet.http.HttpServletRequest

@RunWith(classOf[JUnitRunner])
class SimpleMockHandlerTest extends FunSuite {
  val handler = new SimpleMockHandler
  val exampleMockParameters0 = ListMap("Fruits" -> List("Apple", "Orange"))
  val exampleRequestParameters0 = ListMap("Animals" -> List("Cat")) ++
      exampleMockParameters0

  def initExampleHandler0() = {
    val matcher = new ParameterMatcher(exampleMockParameters0)
    handler.registerMock(new Mock(null, matcher))
  }

  test("MockHandler should return None on unknown request") {
    initExampleHandler0()
    val request = mock(classOf[HttpServletRequest])
    val requestParameterMap = ListMap("UnknownParameter" -> Array("Something"))
    when(request.getParameterMap()) thenReturn requestParameterMap
    assert(handler.getResponseFor(request) === None)
  }

  test("MockHandler should return the response for a known request") {
    initExampleHandler0()
    val request = mock(classOf[HttpServletRequest])
    val requestParameterMap = ListMap("Fruits" -> Array("Apple", "Orange"))
    when(request.getParameterMap()) thenReturn requestParameterMap
    val maybeMockResponse = handler.getResponseFor(request)
    assert(maybeMockResponse != None)
    assert(maybeMockResponse isDefined)
  }
}
