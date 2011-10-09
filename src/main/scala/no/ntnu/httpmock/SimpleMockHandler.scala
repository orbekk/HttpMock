package no.ntnu.httpmock

import java.util.ArrayList
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.collectionAsScalaIterable

class SimpleMockHandler extends MockHandler {
  private val mocks = new ArrayList[Mock]

  override def getResponseFor(request: HttpServletRequest):
      Option[DummyMockResponse] = {
    val maybeMock = mocks find {_ matches request}
    maybeMock match {
      case Some(mock) => Some(DummyMockResponse())
      case None => None
    }
  }

  def registerMock(mock: Mock) = {
    mocks add mock
  }
}
