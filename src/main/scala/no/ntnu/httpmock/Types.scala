package no.ntnu.httpmock

import no.ntnu.httpmock.matcher.RequestMatcher

object Types {
  case class MockRequest(matcher: RequestMatcher)
  case class MockResponse(data: String)
}
