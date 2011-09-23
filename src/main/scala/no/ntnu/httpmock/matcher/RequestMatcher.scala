package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest

trait RequestMatcher {
  def matches(request: HttpServletRequest): Boolean
}