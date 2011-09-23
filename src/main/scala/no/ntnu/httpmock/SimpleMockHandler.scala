package no.ntnu.httpmock

import java.util.ArrayList
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.collectionAsScalaIterable

class SimpleMockHandler extends MockHandler {
  private val mocks = new ArrayList[(Types.MockRequest, Types.MockResponse)]

  override def getResponseFor(request: HttpServletRequest):
      Option[Types.MockResponse] = {
    mocks find {mock => mock._1.matcher.matches(request)}
    None
  }

  def registerMock(request: Types.MockRequest, response: Types.MockResponse) = {
    mocks add ((request, response))
  }
}