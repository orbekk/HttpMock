package no.ntnu.httpmock

import Types.{MockRequest, MockResponse}
import javax.servlet.http.HttpServletRequest

trait MockHandler extends MockProvider {
  def registerMock(request: MockRequest, response: MockResponse)
}