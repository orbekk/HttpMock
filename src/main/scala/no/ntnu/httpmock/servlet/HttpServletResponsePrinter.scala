package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.StringBuilder

/**
 * Pretty print HttpServletResponse.
 */
object HttpServletResponsePrinter extends Logger {
  /**
   * Print an HttpServletResponse and return it as a String.
   */
  def print(response: HttpServletResponse): String = {
    val builder: StringBuilder = new StringBuilder
    logger.info("Printing response " + response)
    response match {
      case loggingResponse: LoggingHttpServletResponse =>
        internalPrint(builder, loggingResponse)
      case _ => 
        builder.append("Unable to print response: ")
        builder.append("Must be a LoggingHttpServletResponse.")
    }
    return builder.toString()
  }

  private def internalPrint(builder: StringBuilder,
      response: LoggingHttpServletResponse) {
    addStatusLine(builder, response)
    addHeaders(builder, response)
    builder.append("\n\n")
    builder.append(response.getContent())
  }

  private def addHeaders(builder: StringBuilder,
      response: LoggingHttpServletResponse) {
    response.parameters.foreach { case (name, values) =>
      values.foreach { value =>
        builder.append(name + ": " + value + "\n")
      }
    }
  }

  private def addStatusLine(builder: StringBuilder,
      response: LoggingHttpServletResponse) {
    builder.append(response.protocol)
    builder.append(" ")
    builder.append(response.status)
    builder.append(" ")
    builder.append(response.statusMessage)
    builder.append("\n")
  }
}
