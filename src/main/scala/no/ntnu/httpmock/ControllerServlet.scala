package no.ntnu.httpmock

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ControllerServlet(mockHandler: MockHandler) extends HttpServlet {
  override protected def doGet(request: HttpServletRequest,
      response: HttpServletResponse) = {
    val path = ServletHelper.getServletRelativePath(request)
    
    path match {
      case "/" => defaultPage(request, response)
      case _ => response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }
  }
  
  override protected def doPost(request: HttpServletRequest,
      response: HttpServletResponse) = {
    try {
      val requestString = ServletHelper.getParameter("request", request)
      val responseString = ServletHelper.getParameter("response", request)
      mockHandler.registerMock(Types.MockRequest(requestString),
          Types.MockResponse(responseString))
    } catch {
      case e:ServletHelper.ParameterNotFoundException =>
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
            "missing parameter: " + e.getMessage)
    }
  }
  
  def defaultPage(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter().println("Current mock calls registered:")
    for (val (mockRequest, mockResponse) <- mockHandler.getMockMap()) {
       response.getWriter().println(mockRequest + " => " + mockResponse)
    }
  }
}