package no.ntnu.httpmock.servlet

import javax.servlet.http.HttpServletResponse
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap
import scala.collection.JavaConversions.mapAsJavaMap

@RunWith(classOf[JUnitRunner])
class HttpServletResponsePrinterTest extends FunSuite {
  test("Prints HTTP response.") {
    pendingUntilFixed {
      val response: HttpServletResponse = mock(classOf[HttpServletResponse])
      val responseString: String = HttpServletResponsePrinter.print(response)

      assert(responseString startsWith "HTTP/1.1 200")
    }
  }
}
