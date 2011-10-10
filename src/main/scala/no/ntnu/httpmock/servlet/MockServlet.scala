package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import no.ntnu.httpmock.Mock
import no.ntnu.httpmock.MockProvider
import no.ntnu.httpmock.DummyMockResponse

class MockServlet(mockProvider: MockProvider)
    extends HttpServlet with Logger {
  override protected def doGet(request: HttpServletRequest,
      response: HttpServletResponse) {
    val requestString = request.getRequestURI()
    mockProvider.getResponseFor(request) match {
      case Some(mock) => serve(request, response, mock)
      case None => unexpectedCall(request, response, requestString)
    }
  }

  private def serve(request: HttpServletRequest, response: HttpServletResponse,
      mock: Mock) {
    logger.info("Serving mock: " + mock.descriptor)
    mock.writeResponseTo(response)
  }

  private def unexpectedCall(request: HttpServletRequest,
      response: HttpServletResponse, requestString: String) {
    logger.warning("Unexpected call: " + requestString + ". " +
        "Note: In future versions this will give a verification error, but " +
        "verification is not yet supported")
    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unexpected call")
  }
}
