package no.ntnu.httpmock.http

import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager

object RequestForwarder {
  /**
   * Create a RequestForwarder with a given baseUri.
   */
  def create(): RequestForwarder = {
    val httpClient = new DefaultHttpClient(
      new ThreadSafeClientConnManager())
    new RequestForwarder(httpClient)
  }
}

/**
 * A RequestForwarder forwards HttpServletRequests to another host.
 */
class RequestForwarder(httpClient: HttpClient) {
  def execute(baseUri: URI, request: HttpServletRequest,
      response: HttpServletResponse) {
    val newUrl = new URI(baseUri + request.getRequestURI())
    val converter = new HttpServletRequestConverter(newUrl)
    val httpRequest = converter.convertRequest(request)
    val httpResponse = httpClient.execute(httpRequest)
    (new ResponseHandler).handle(httpResponse, response)
    response.addHeader("X-HTTPMOCK-FORWARDING-URI", newUrl.toString())
  }
}
