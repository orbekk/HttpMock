package no.ntnu.httpmock.servlet

import java.net.URLEncoder
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.StringBuilder

/**
 * Pretty print HttpServletRequests.
 */
object HttpServletRequestPrinter {
  /**
   * Print an HttpServletRequest and return it as a string.
   */
  def print(request: HttpServletRequest): String = {
    val builder: StringBuilder = new StringBuilder    

    addRequestLine(builder, request)

    return builder.toString()
  }

  private def encode(s: String) = URLEncoder.encode(s, "utf-8")

  private def addRequestLine(builder: StringBuilder,
      request: HttpServletRequest) {
    builder.append(request.getMethod())
    builder.append(" ")
    builder.append(request.getRequestURI())
    addParameterList(builder, request)
    builder.append(" ")
    builder.append(request.getProtocol())
    builder.append("\n")
  }

  private def addParameterList(builder: StringBuilder,
    request: HttpServletRequest) {
    builder.append("?")
    type J = java.util.Map[String, Array[String]]
    for ((name, values) <- request.getParameterMap().asInstanceOf[J]) {
      for (value <- values) {
        builder.append(encode(name))
        builder.append("=")
        builder.append(encode(value))
        builder.append("&")
      }
    }
    builder.deleteCharAt(builder.length - 1)
  }
}
