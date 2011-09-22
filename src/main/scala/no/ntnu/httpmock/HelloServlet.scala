package no.ntnu.httpmock

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HelloServlet extends HttpServlet {

  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter().print("Hello, World!\n")
    response.getWriter().print(request.getRequestURI())
  }

}