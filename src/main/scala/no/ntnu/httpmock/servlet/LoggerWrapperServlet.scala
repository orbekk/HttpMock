package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object LoggerWrapperServlet {
  trait RequestLogger {
    def log(tag: String, request: HttpServletRequest, response: HttpServletResponse) {
    }
  }

  /**
   * A ContextRequestLogger handles scoping of log tags.
   */
  class ContextRequestLogger(requestLogger: RequestLogger) {
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

    def withScope(tag: String)(f: RequestLogger => Unit) {
      pushScope(tag)
      f(getRequestLogger())
      popScope()
    }

    private def getRequestLogger(): RequestLogger = new RequestLogger() {
      override def log(tag: String, request:HttpServletRequest, response:HttpServletResponse) {
        if (!requestWritten) {
          requestLogger.log(tag, request, response)
          requestWritten = true
        }
      }
    }
  }
}

class LoggerWrapperServlet(
    contextRequestLogger: LoggerWrapperServlet.ContextRequestLogger,
    wrappedServlet: HttpServlet, tag: String) extends HttpServlet
    with Logger {

  override def service(request: HttpServletRequest, response: HttpServletResponse) {
    contextRequestLogger.withScope(tag) { logger =>
      wrappedServlet.service(request, response)
    }
  }
}
