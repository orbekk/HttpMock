package no.ntnu.httpmock.http

import javax.servlet.http.HttpServletResponse
import org.apache.http.HttpResponse
import org.apache.http.Header
import org.apache.http.HttpEntity

/**
 * A response handler that populates a HttpServletResponse.
 */
class ResponseHandler {
  def handle(httpResponse: HttpResponse,
      servletResponse: HttpServletResponse) {
    if (httpResponse.getEntity() != null) {
      httpResponse.getEntity().writeTo(servletResponse.getOutputStream())
    }

    servletResponse.setStatus(httpResponse.getStatusLine().getStatusCode())
    httpResponse.getAllHeaders() foreach { header: Header =>
      servletResponse.addHeader(header.getName(), header.getValue())
    }
  }
}
