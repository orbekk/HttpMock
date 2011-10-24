package no.ntnu.httpmock.servlet

import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.StringBuilder

/**
 * Pretty print HttpServletResponse.
 */
object HttpServletResponsePrinter {
  /**
   * Print an HttpServletResponse and return it as a String.
   */
  def print(response: HttpServletResponse): String = {
    val builder: StringBuilder = new StringBuilder
    response match {
      case loggingResponse: LoggingHttpServletResponse =>
        internalPrint(builder, loggingResponse)
      case _ => 
        builder.append("Unable to print response: ")
        builder.append("Must be a LoggingHttpServletResponse.")
    }
    return builder.toString()
  }

  def internalPrint(builder: StringBuilder,
      response: LoggingHttpServletResponse) {
    addStatusLine(builder, response)
  }

  def addStatusLine(builder: StringBuilder,
      response: LoggingHttpServletResponse) {
    builder.append(response.method)
    builder.append(" ")
    builder.append(response.status)
  }
}
