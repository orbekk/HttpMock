package no.ntnu.httpmock.http

import java.net.URI
import javax.servlet.http.HttpServletRequest
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpUriRequest
import scala.collection.JavaConversions.mapAsScalaMap

/**
 * Converts an HttpServletRequest to a HttpUriRequest.
 *
 * Adds a different base url to the new request. This can be used for
 * forwarding.
 */
class HttpServletRequestConverter(newBaseUri: String) {
  def convertRequest(request: HttpServletRequest): HttpUriRequest = {
    val convertedRequest = new HttpEntityEnclosingRequestBase() {
      def getMethod(): String = request.getMethod()
    }
    convertedRequest.setURI(
        new URI(newBaseUri + request.getRequestURI()))

    val javaParameters = request.getParameterMap()
        .asInstanceOf[java.util.Map[String, Array[String]]]
    javaParameters.toMap foreach { case (name, values) =>
      values foreach { value =>
        convertedRequest.addHeader(name, value)
      }
    }
    // TODO: Add entity.

    convertedRequest
  }
}
