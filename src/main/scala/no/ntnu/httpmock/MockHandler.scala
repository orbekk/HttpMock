package no.ntnu.httpmock

import Types.{MockRequest, MockResponse}

trait MockHandler {
  def getResponseFor(request: MockRequest): Option[MockResponse]
  def registerMock(request: MockRequest, response: MockResponse)
  def getMockMap(): Map[MockRequest, MockResponse]
}