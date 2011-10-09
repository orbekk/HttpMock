package no.ntnu.httpmock

import no.ntnu.httpmock.servlet.ControllerServlet
import no.ntnu.httpmock.servlet.MockServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

object Main extends App {
  def startServer(port: Int): Server = {
    val server = new Server(port)
    val context = new ServletContextHandler(server, "/")

    val controller = new ControllerServlet(new SimpleMockHandler)
    val mockServlet = new MockServlet(controller)
    
    context.addServlet(new ServletHolder(controller), "/_httpmock/*")
    context.addServlet(new ServletHolder(mockServlet), "/*")
    server.start
    server
  }

  Console.println("Hello Scala!")
  val server = startServer(8080)
  server.join()
}
