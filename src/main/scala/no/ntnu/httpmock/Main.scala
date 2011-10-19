package no.ntnu.httpmock

import java.net.URI
import javax.servlet.http.HttpServlet
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
  def startServer(port: Int): Server = {
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

    val mockServlet = new MockServlet(controller, wrappedUnexpectedCallServlet)
    val wrappedMockServlet = wrap(mockServlet, "mock")
    
    context.addServlet(new ServletHolder(logServlet), "/_httpmock/log")
    context.addServlet(new ServletHolder(controller), "/_httpmock/*")
    context.addServlet(new ServletHolder(wrappedMockServlet), "/*")
    server.start
    server
  }

  Console.println("Hello Scala!")
  val server = startServer(8080)
  server.join()
}
