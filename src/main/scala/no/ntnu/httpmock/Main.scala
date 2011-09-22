package no.ntnu.httpmock

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler

object Main extends App {
  def startServer(port: Int): Server = {
    val server = new Server(port)
    val context = new ServletContextHandler(server, "/")
    context.addServlet(classOf[HelloServlet], "/*")
    server.start
    server
  }

  Console.println("Hello Scala!")
  val server = startServer(8080)
  server.join()
}
