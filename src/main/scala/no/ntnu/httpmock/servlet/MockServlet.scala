package no.ntnu.httpmock

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.orbekk.logging.Logger

class MockServlet(mockProvider: MockProvider)
    extends HttpServlet with Logger {
  override protected def doGet(request: HttpServletRequest,
      response: HttpServletResponse) {
    val requestString = request.getRequestURI()
    val maybeMockResponse = mockProvider.getResponseFor(request)
    maybeMockResponse match {
      case Some(mockResponse) => serve(request, response, mockResponse)
      case None => unexpectedCall(request, response, requestString)
    }
  }

  private def serve(request: HttpServletRequest,
      response: HttpServletResponse, mockResponse: Types.MockResponse) {
    response.getWriter().println(mockResponse.data)
  }

  private def unexpectedCall(request: HttpServletRequest,
      response: HttpServletResponse, requestString: String) {
    logger.warning("Unexpected call: " + requestString + ". " +
        "Note: In future versions this will give a verification error, but " +
        "verification is not yet supported")
    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unexpected call")
  }
}