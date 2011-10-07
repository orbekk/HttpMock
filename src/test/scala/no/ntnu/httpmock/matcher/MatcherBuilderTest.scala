package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MatcherBuilderTest extends FunSuite {
  test("withPath() should return an appropriate PathMatcher") {
    val matcher = (new MatcherBuilder)
        .withPath("/my/test/path")
        .build()
    val matchingRequest = mock(classOf[HttpServletRequest])
    when(matchingRequest.getRequestURI()) thenReturn "/my/test/path"
    assert(matcher matches matchingRequest)

    val nonMatchingRequest = mock(classOf[HttpServletRequest])
    when(nonMatchingRequest.getRequestURI()) thenReturn "/some/other/path"
    assert(!(matcher matches nonMatchingRequest))
  }
}
