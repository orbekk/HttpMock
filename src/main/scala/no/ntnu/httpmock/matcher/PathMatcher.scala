package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest

class PathMatcher(path: String) extends RequestMatcher {
  override def matches(request: HttpServletRequest): Boolean =
    request.getRequestURI() == path
}
