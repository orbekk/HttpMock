package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class MatcherBuilderTest extends FunSuite {
  // TODO: This is a hack. For some reason we need to cast getParameterMap() to
  // this type, which should already be implied.
  type J = java.util.Map[String, Array[String]]

  test("withPath() should return an appropriate PathMatcher") {
    val matcher = MatcherBuilder()
        .withPath("/my/test/path")
        .build()
    val matchingRequest = mock(classOf[HttpServletRequest])
    when(matchingRequest.getRequestURI()) thenReturn "/my/test/path"
    assert(matcher matches matchingRequest)

    val nonMatchingRequest = mock(classOf[HttpServletRequest])
    when(nonMatchingRequest.getRequestURI()) thenReturn "/some/other/path"
    assert(!(matcher matches nonMatchingRequest))
  }

  test("withParameters() should return working ParameterMatcher") {
    val parameters = ListMap("myParam" -> List("val1", "val2"))
    val javaParameters : java.util.Map[String, Array[String]] =
        parameters mapValues (x => Array(x toSeq : _ *))
    val matcher = MatcherBuilder()
        .withParameters(parameters)
        .build()
    val matchingRequest = mock(classOf[HttpServletRequest])
    when(matchingRequest.getParameterMap().asInstanceOf[J])
        .thenReturn(javaParameters)
    assert(matcher matches matchingRequest)
  }

  test("Matchers can be combined") {
    val parameters = ListMap("myParam" -> List("some value"))
    val javaParameters : java.util.Map[String, Array[String]] =
        parameters mapValues (x => Array(x toSeq : _ *))
    val matcher = MatcherBuilder()
        .withPath("/my/test/path")
        .withParameters(parameters)
        .build()

    val matchingRequest = mock(classOf[HttpServletRequest])
    when(matchingRequest.getRequestURI()) thenReturn "/my/test/path"
    when(matchingRequest.getParameterMap().asInstanceOf[J])
        .thenReturn(javaParameters)
    assert(matcher matches matchingRequest)

    val nonMatchingRequest1 = mock(classOf[HttpServletRequest])
    when(nonMatchingRequest1.getRequestURI()) thenReturn "/wrong/path/"
    when(nonMatchingRequest1.getParameterMap().asInstanceOf[J])
        .thenReturn(javaParameters)
    assert(!(matcher matches nonMatchingRequest1))

    val nonMatchingRequest2 = mock(classOf[HttpServletRequest])
    when(nonMatchingRequest2.getRequestURI()) thenReturn "/my/test/path"
    val wrongParameters = ListMap("param" -> Array("val"))
    when(nonMatchingRequest2.getParameterMap().asInstanceOf[J])
        .thenReturn(wrongParameters)
    assert(!(matcher matches nonMatchingRequest2))
  }
}
