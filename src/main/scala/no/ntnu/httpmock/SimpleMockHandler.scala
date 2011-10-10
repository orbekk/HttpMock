package no.ntnu.httpmock

import java.util.ArrayList
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.collectionAsScalaIterable

class SimpleMockHandler extends MockHandler {
  private val mocks = new ArrayList[Mock]

  override def getResponseFor(request: HttpServletRequest): Option[Mock] =
    mocks find {_ matches request}

  def registerMock(mock: Mock) = mocks add mock
}
