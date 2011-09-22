package no.ntnu.httpmock

object Types {
  case class MockRequest(data: String)
  case class MockResponse(data: String)
}