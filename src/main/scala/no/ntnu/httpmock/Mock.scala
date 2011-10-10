package no.ntnu.httpmock

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import no.ntnu.httpmock.matcher.RequestMatcher

class Mock(val descriptor: MockDescriptor, matcher: RequestMatcher)
    extends RequestMatcher {
  def matches(request: HttpServletRequest) = matcher.matches(request)

  def writeResponseTo(response: HttpServletResponse) =
      descriptor.response.writeResponseTo(response)
}
