package no.ntnu.httpmock.servlet

import java.io.StringWriter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper
import scala.collection.mutable.HashMap

object LoggingHttpServletResponse {
  /**
   * Create a LoggingHttpServletResponse from a request and response context.
   *
   * Copies some metadata (e.g. the protocol) from the request and returns an
   * HttpServletResponse that collects the data that is passed into it.
   */
  def create(request: HttpServletRequest, response: HttpServletResponse):
      LoggingHttpServletResponse = {
    new LoggingHttpServletResponse(method = request.getMethod(),
        response = response)
  }
}

class LoggingHttpServletResponse(val method: String,
    val response: HttpServletResponse)
    extends HttpServletResponseWrapper(response) {
  val parameters = new HashMap[String, List[String]]
  var status = 200
  var statusMessage = "OK"

  override def sendError(error: Int) {
    status = error
    statusMessage = "<SOME ERROR>"
    super.sendError(error)
  }

  override def sendError(error: Int, message: String) {
    status = error
    statusMessage = message
    super.sendError(error, message)
  }

  override def addHeader(name: String, value: String) {
    val oldValues = parameters.getOrElse(name, List[String]())
    parameters.put(name, oldValues :+ value) 
    super.addHeader(name, value)
  }
}
