package no.ntnu.httpmock.http

import java.net.URLEncoder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpUriRequest
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.StringBuilder

/**
 * Converts an HttpServletRequest to a HttpUriRequest.
 *
 * Adds a different base url to the new request. This can be used for
 * forwarding.
 */
class HttpServletRequestConverter(newUri: URI) {
  def convertRequest(request: HttpServletRequest): HttpUriRequest = {
    val convertedRequest = new HttpEntityEnclosingRequestBase() {
      def getMethod(): String = request.getMethod()
    }
    convertedRequest.setURI(new URI(newUri.toString() +
        getParameterList(request)))

    val javaParameters = request.getParameterMap()
        .asInstanceOf[java.util.Map[String, Array[String]]]
    javaParameters.toMap foreach { case (name, values) =>
      values foreach { value =>
        convertedRequest.getParams().setParameter(name, value)
      }
    }
    // TODO: Add entity.

    convertedRequest
  }

  private def encode(s: String) = URLEncoder.encode(s, "utf-8")

  private def getParameterList(request: HttpServletRequest): String = {
    val builder = new StringBuilder
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
    return builder.toString()
  }
}
