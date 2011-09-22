package no.ntnu.httpmock

trait MockProvider {
  def getResponseFor(request: Types.MockRequest): Option[Types.MockResponse]
}