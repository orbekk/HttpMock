package no.ntnu.httpmock

trait MockHandler extends MockProvider {
  def registerMock(mock: Mock)
}
