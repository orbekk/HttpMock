package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object LoggerWrapperServlet {
  /**
   * A ContextRequestLogger handles scoping of log tags.
   */
  class ContextRequestLogger(requestLogger: LogServlet.RequestLogger) {
    var scope: List[String] = List()
    var requestWritten = false

    private def pushScope(tag: String) {
      if (scope.isEmpty) {
        requestWritten = false
      }
      scope = scope :+ tag
    }

    private def popScope() {
      scope = scope.init
    }

    def withScope(tag: String)(f: LogServlet.RequestLogger => Unit) {
      pushScope(tag)
      f(getRequestLogger())
      popScope()
    }

    private def getRequestLogger(): LogServlet.RequestLogger =
        new LogServlet.RequestLogger() {
      override def log(tag: String, request:HttpServletRequest,
          response:HttpServletResponse) {
        if (!requestWritten) {
          requestLogger.log(tag, request, response)
          requestWritten = true
        }
      }
    }
  }
}

/**
 * This class performs various hacks that helps logging.
 * 
 * Not thread-safe.
 *
 * TODO: Make it thread-safe?
 */
class LoggerWrapperServlet(
    contextRequestLogger: LoggerWrapperServlet.ContextRequestLogger,
    wrappedServlet: HttpServlet, tag: String) extends HttpServlet
    with Logger {

  /**
   * Wraps the HttpServletResponse in a LoggingHttpServletRespnose
   * if that has not already been done by a parent LoggerWrapperServlet.
   */
  def convertResponse(request: HttpServletRequest,
      response: HttpServletResponse): LoggingHttpServletResponse = {
    response match {
      case loggingResponse: LoggingHttpServletResponse => loggingResponse
      case _ => LoggingHttpServletResponse.create(request, response)
    }
  }

  override def service(request: HttpServletRequest,
      response: HttpServletResponse) {
    val wrappedResponse: LoggingHttpServletResponse =
        convertResponse(request, response)
    contextRequestLogger.withScope(tag) { logger =>
      wrappedServlet.service(request, wrappedResponse)
      logger.log(tag, request, wrappedResponse)
    }
  }
}
