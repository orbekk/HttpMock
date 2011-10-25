package no.ntnu.httpmock.servlet

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap
import scala.collection.JavaConversions.mapAsJavaMap

@RunWith(classOf[JUnitRunner])
class HttpServletResponsePrinterTest extends FunSuite with Logger {
  test("Unable to print unless response is logging.") {
    val response: HttpServletResponse = mock(classOf[HttpServletResponse])
    val responseString: String = HttpServletResponsePrinter.print(response)
   
    assert(responseString startsWith "Unable to print")
  }

  test("Prints HTTP response.") {
    val request: HttpServletRequest = mock(classOf[HttpServletRequest])
    when(request.getProtocol()) thenReturn "HTTP/1.1"

    val response0: HttpServletResponse = mock(classOf[HttpServletResponse])
    val response: LoggingHttpServletResponse = 
        LoggingHttpServletResponse.create(request, response0)
    val responseString: String = HttpServletResponsePrinter.print(response)

    logger.info("Got response string: " + responseString)
    assert(responseString startsWith "HTTP/1.1 200")
  }
}
