package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import no.ntnu.httpmock.DummyMockResponse
import no.ntnu.httpmock.Mock
import no.ntnu.httpmock.MockDescriptor
import no.ntnu.httpmock.MockHandler
import no.ntnu.httpmock.MockProvider
import no.ntnu.httpmock.matcher.ParameterMatcher
import scala.collection.JavaConversions.mapAsScalaMap

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
      val descriptor = MockDescriptor.parseFromRequest(request.getReader())
      if (descriptor != null) {
        logger.info("Registering mock: " + descriptor toString)
        val matcher = descriptor.buildMatcher()
        val mock = new Mock(descriptor, matcher)
        // val mockRequest = Types.MockRequest(matcher)
        // val mockResponse = Types.MockResponse("TODO: Implement mock responses.")
        mockHandler.registerMock(mock)
      } else {
        logger.warning("MockDescriptor did not parse")
      }
    } catch {
      case e:ServletHelper.ParameterNotFoundException =>
        logger.warning("Got error: " + e.getMessage)
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "missing parameter: " + e.getMessage)
    }
  }

  def getResponseFor(request: HttpServletRequest): Option[Mock] =
    mockHandler.getResponseFor(request)
}
