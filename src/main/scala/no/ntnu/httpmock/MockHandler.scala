package no.ntnu.httpmock

import Types.{MockRequest, MockResponse}

trait MockHandler extends MockProvider {
  def registerMock(request: MockRequest, response: MockResponse)
  def getMockMap(): Map[MockRequest, MockResponse]
}