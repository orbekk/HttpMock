package no.ntnu.httpmock.servlet

import javax.servlet.http.HttpServletRequest
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.ListMap
import scala.collection.JavaConversions.mapAsJavaMap

@RunWith(classOf[JUnitRunner])
class HttpServletRequestPrinterTest extends FunSuite {
  // TODO: This is a hack. For some reason we need to cast getParameterMap() to
  // this type, which should already be implied.
  type J = java.util.Map[String, Array[String]]

  test("Prints HTTP request.") {
    val request: HttpServletRequest = mock(classOf[HttpServletRequest])
    when(request.getMethod).thenReturn("GET")
    when(request.getRequestURI()).thenReturn("/MyUrl")
    val parameters: J = ListMap[String, Array[String]](
        "a" -> Array("b"))
    when(request.getParameterMap().asInstanceOf[J])
        .thenReturn(parameters)
    when(request.getProtocol()).thenReturn("HTTP/1.1")
    
    val requestString: String = HttpServletRequestPrinter.print(request)
    assert(requestString startsWith "GET /MyUrl?a=b HTTP/1.1")
  }
}
