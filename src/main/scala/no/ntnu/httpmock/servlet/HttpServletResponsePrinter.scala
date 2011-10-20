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
    // TODO: In order to print a HttpServletResponse, we have to provide a
    // wrapper that captures the data we want from the servlet.
    "Unable to print response"
  }
}
