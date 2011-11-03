package no.ntnu.httpmock

import java.lang.Integer
import java.net.URI
import javax.servlet.http.HttpServlet
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import no.ntnu.httpmock.servlet.ControllerServlet
import no.ntnu.httpmock.servlet.LogServlet
import no.ntnu.httpmock.servlet.LoggerWrapperServlet
import no.ntnu.httpmock.servlet.MockServlet
import no.ntnu.httpmock.servlet.NullServlet
import no.ntnu.httpmock.servlet.ProxyServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

object Main extends App {
  def startServer(port: Int, useProxy: Boolean, proxyUrl: String): Server = {
    val server = new Server(port)
    val context = new ServletContextHandler(server, "/")

    val controller = new ControllerServlet(new SimpleMockHandler)
    val logServlet = LogServlet.create()

    val logContext = new LoggerWrapperServlet.ContextRequestLogger(
        logServlet.logger)

    def wrap(servlet: HttpServlet, tag: String) =
        new LoggerWrapperServlet(logContext, servlet, tag)

    val unexpectedCallServlet = new NullServlet
    val wrappedUnexpectedCallServlet =
        wrap(unexpectedCallServlet, "unexpected")
    
    // TODO: I can't imagine not wanting to ignore /favicon.ico, but
    // we may need a smarter way to ignore certain paths.
    context.addServlet(new ServletHolder(new NullServlet), "/favicon.ico")

    context.addServlet(new ServletHolder(logServlet), "/_httpmock/log")
    context.addServlet(new ServletHolder(controller), "/_httpmock/*")
    if (useProxy) {
      val proxyServlet = ProxyServlet.create(new URI(proxyUrl))
      val wrappedProxyServlet = wrap(proxyServlet, "proxy")
      context.addServlet(new ServletHolder(wrappedProxyServlet), "/*") 
    } else {
      val mockServlet = new MockServlet(controller, wrappedUnexpectedCallServlet)
      val wrappedMockServlet = wrap(mockServlet, "mock")
      context.addServlet(new ServletHolder(wrappedMockServlet), "/*")
    }
    server.start
    server
  }

  override def main(args: Array[String]) = {
    val parser = new OptionParser
    parser.accepts("use-proxy")
    val port: OptionSpec[Integer] =
      parser.accepts("port").withRequiredArg().ofType(classOf[Integer]).required()
    val proxyUrl: OptionSpec[String] =
    parser.accepts("proxy-url").withRequiredArg().ofType(classOf[String])
    val options: OptionSet = parser.parse(args: _*)

    val server = startServer(options.valueOf(port),
        options.has("use-proxy"), options.valueOf(proxyUrl))
    server.join()
  }
}
