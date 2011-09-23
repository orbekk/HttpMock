package no.ntnu.httpmock
import javax.servlet.http.HttpServletRequest

trait MockProvider {
  def getResponseFor(request: HttpServletRequest): Option[Types.MockResponse]
}