package no.ntnu.httpmock.servlet

import java.net.URI
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import no.ntnu.httpmock.http.RequestForwarder

object ProxyServlet {
  def create(baseUri: URI): ProxyServlet = {
    new ProxyServlet(RequestForwarder.create(), baseUri)
  }
}

class ProxyServlet(forwarder: RequestForwarder, baseUri: URI)
    extends HttpServlet {
  override def service(request: HttpServletRequest, response: HttpServletResponse) {
    forwarder.execute(baseUri, request, response)
  }
}
