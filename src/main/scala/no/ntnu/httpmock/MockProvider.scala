package no.ntnu.httpmock

import javax.servlet.http.HttpServletRequest

case class DummyMockResponse()

trait MockProvider {
  def getResponseFor(request: HttpServletRequest): Option[Mock]
}
