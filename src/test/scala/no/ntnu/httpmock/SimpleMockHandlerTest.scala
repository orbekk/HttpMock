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
  // TODO: This is a hack. For some reason we need to cast getParameterMap() to
  // this type, which should already be implied.
  type J = java.util.Map[String, Array[String]]

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
    when(request.getParameterMap().asInstanceOf[J])
        .thenReturn(requestParameterMap)
    assert(handler.getResponseFor(request) === None)
  }

  test("MockHandler should return the response for a known request") {
    initExampleHandler0()
    val request = mock(classOf[HttpServletRequest])
    val requestParameterMap = ListMap("Fruits" -> Array("Apple", "Orange"))
    when(request.getParameterMap().asInstanceOf[J])
        .thenReturn(requestParameterMap)
    val maybeMockResponse = handler.getResponseFor(request)
    assert(maybeMockResponse != None)
    assert(maybeMockResponse isDefined)
  }
}
