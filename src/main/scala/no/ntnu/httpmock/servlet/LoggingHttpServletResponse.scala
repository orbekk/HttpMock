package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import java.io.PrintWriter
import java.io.StringWriter
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper
import no.ntnu.httpmock.io.OutputStreamSplitter
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
    new LoggingHttpServletResponse(protocol = request.getProtocol(),
        response = response)
  }
}

class LoggingHttpServletResponse(val protocol: String,
    val response: HttpServletResponse)
    extends HttpServletResponseWrapper(response) with Logger {
  val parameters = new HashMap[String, List[String]]
  private val outputStream = new OutputStreamSplitter(
    response.getOutputStream())
  private val writer = new PrintWriter(outputStream)
  var status = 200
  var statusMessage = "OK"

  override def getOutputStream(): ServletOutputStream = outputStream

  override def getWriter(): PrintWriter = {
    writer
  }

  override def sendError(error: Int) {
    status = error
    statusMessage = "<SOME ERROR>"
    super.sendError(error)
  }

  override def setStatus(status: Int) {
    this.status = status
    statusMessage = "<Unknown status>"
    super.setStatus(status)
  }

  override def sendError(error: Int, message: String) {
    status = error
    statusMessage = message
    logger.info("SendError from " + this)
    super.sendError(error, message)
  }

  override def addHeader(name: String, value: String) {
    val oldValues = parameters.getOrElse(name, List[String]())
    parameters.put(name, oldValues :+ value) 
    super.addHeader(name, value)
  }

  def getContent(): String = {
    writer.flush()
    outputStream.getContent()
  }
}
