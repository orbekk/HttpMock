package no.ntnu.httpmock

import javax.servlet.http.HttpServletRequest

trait MockHandler extends MockProvider {
  def registerMock(request: Types.MockRequest, response: Types.MockResponse)
}
