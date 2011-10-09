package no.ntnu.httpmock

import javax.servlet.http.HttpServletRequest
import no.ntnu.httpmock.matcher.RequestMatcher

class Mock(val descriptor: MockDescriptor, val matcher: RequestMatcher)
    extends RequestMatcher {
  def matches(request: HttpServletRequest) = matcher.matches(request)
}
