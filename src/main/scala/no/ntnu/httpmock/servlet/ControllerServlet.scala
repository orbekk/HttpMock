package no.ntnu.httpmock

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import no.ntnu.httpmock.matcher.ParameterMatcher
import scala.collection.JavaConversions.mapAsScalaMap
import com.orbekk.logging.Logger

class ControllerServlet(mockHandler: MockHandler) extends HttpServlet
  with MockProvider with Logger {
  override protected def doGet(request: HttpServletRequest,
      response: HttpServletResponse) {
    val path = ServletHelper.getServletRelativePath(request)

    path match {
      case "/" => defaultPage(request, response)
      case _ => response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }
  }

  override protected def doPost(request: HttpServletRequest,
      response: HttpServletResponse) {
    val path = ServletHelper.getServletRelativePath(request)

    path match {
      case "/set" => setMock(request, response)
      case _ => response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }
  }

  def defaultPage(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter().println("Current mock calls registered:")
//    for ((mockRequest, mockResponse) <- mockHandler.getMockMap()) {
//       response.getWriter().println(mockRequest + " => " + mockResponse)
//    }
  }

  def setMock(request: HttpServletRequest, response: HttpServletResponse) {
    try {
      val mockRequest_ = MockRequest.parseFromRequest(request.getReader())
      logger.info(mockRequest_ toString)

      val mockRequest = Types.MockRequest(
          ParameterMatcher.fromJavaParameterMap(request.getParameterMap().toMap))
      val mockResponse = Types.MockResponse("TODO: Implement mock responses.")
      logger.info("Setting up mock: " + mockResponse)

      mockHandler.registerMock(mockRequest, mockResponse)
    } catch {
      case e:ServletHelper.ParameterNotFoundException =>
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "missing parameter: " + e.getMessage)
    }
  }

  def getResponseFor(request: HttpServletRequest): Option[Types.MockResponse] =
    mockHandler.getResponseFor(request)
}
