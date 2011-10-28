package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import scala.collection.mutable.ArrayBuffer
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object LogServlet {
  trait RequestLogger {
    def log(tag: String, request: HttpServletRequest, response: HttpServletResponse) {
    }
  }

  class DefaultRequestLogger extends RequestLogger {
    case class LogEntry(val tag: String, val request: String,
      val response: String)

    object LogEntry {
      def create(tag: String, request: HttpServletRequest,
        response: HttpServletResponse): LogEntry = {
        LogEntry(tag = tag,
            request = HttpServletRequestPrinter.print(request),
            response = HttpServletResponsePrinter.print(response))
      }
    }

    val logEntries: ArrayBuffer[LogEntry] = ArrayBuffer()

    override def log(tag: String, request: HttpServletRequest,
        response: HttpServletResponse) {
      logEntries.append(LogEntry.create(tag, request, response))
    }
  }

  def create(): LogServlet = {
    new LogServlet(new DefaultRequestLogger)
  }
}

class LogServlet(val logger: LogServlet.DefaultRequestLogger)
    extends HttpServlet {
  override protected def doGet(request: HttpServletRequest,
    response: HttpServletResponse) {
      def println(s: String) { response.getWriter().println(s) }

      println("<html>")
      println("<head>")
      println("<style>")
      println("td { vertical-align: top; padding: 0.2em; };")
      println("</style>")
      println("</head>")
      println("<body>")
      println("<table>")
      println("<tr><td>Tag</td><td>Request</td><td>Response</td></tr>")

      logger.logEntries foreach { entry =>
        println("<tr>")
        println("<td>" + entry.tag + "</td>")
        println("<td><pre>" + escape(entry.request) + "</pre></td>")
        println("<td><pre>" + escape(entry.response) + "</pre></td>")
        println("</tr>")
      }

      println("</table>")
      println("</body>")
  }

  def escape(message: String): String = {
    message.replaceAll("<", "&lt;")
  }
}
