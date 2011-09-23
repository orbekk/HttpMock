package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest

trait Matcher {
  def matches(request: HttpServletRequest): Boolean
}