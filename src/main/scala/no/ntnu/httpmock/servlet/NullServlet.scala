package no.ntnu.httpmock.servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * A NullServlet doesn't do anything, but can be useful for logging.
 */
class NullServlet extends HttpServlet {
  override def service(request: HttpServletRequest,
      response: HttpServletResponse) {
  }
}
