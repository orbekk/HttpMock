package no.ntnu.httpmock

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, when, verify, never}
import org.mockito.Matchers
import org.mockito.Matchers.{anyInt, anyString}
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import scala.collection.immutable.ListMap
import scala.collection.JavaConversions.mapAsJavaMap
import org.scalatest.BeforeAndAfterEach
import java.io.StringWriter
import java.io.PrintWriter

@RunWith(classOf[JUnitRunner])
class FunctionalTest extends FunSuite with BeforeAndAfterEach {
  var controller: ControllerServlet = _
  var mockServlet: MockServlet = _

  override def beforeEach() {
    controller = new ControllerServlet(new SimpleMockHandler)
    mockServlet = new MockServlet(controller)
  }

  /** Configure the request parameters for the mock request.
   *
   * TODO: Add support for getParamerer().
   */
  def setRequestParameters(request: HttpServletRequest, elems: (String, Array[String])*) {
    val parameterMap = ListMap() ++ elems
    when(request.getParameterMap()) thenReturn parameterMap
  }

  def checkSuccess(response: HttpServletResponse) {
    verify(response, never()).sendError(anyInt(), anyString())
  }

  test("No response from MockServlet before configuration") {
    val request = mock(classOf[HttpServletRequest])
    val response = mock(classOf[HttpServletResponse])
    when(request.getMethod()) thenReturn "GET"
    mockServlet.service(request, response)
    verify(response).sendError(Matchers.eq(HttpServletResponse.SC_FORBIDDEN), anyString())
  }

  test("Response from MockServlet after configuration") {
    val controllerRequest = mock(classOf[HttpServletRequest])
    val controllerResponse = mock(classOf[HttpServletResponse])
    when(controllerRequest.getServletPath()) thenReturn "/_httpmock_control_test"
    when(controllerRequest.getRequestURI()) thenReturn "/_httpmock_control_test/set"

    setRequestParameters(controllerRequest, "SomeParameter" -> Array("Value"))
    when(controllerRequest.getMethod()) thenReturn "POST"
    controller.service(controllerRequest, controllerResponse)
    checkSuccess(controllerResponse)

    val request = mock(classOf[HttpServletRequest])
    val response = mock(classOf[HttpServletResponse])
    when(request.getMethod()) thenReturn "GET"
    val writer = new StringWriter()
    when(response.getWriter()) thenReturn (new PrintWriter(writer))
    setRequestParameters(request, "SomeParameter" -> Array("Value"))
    mockServlet.service(request, response)
    checkSuccess(response)
    // TODO: When mock responses has been implemented, verify the response:
    // assert(writer.toString() === "MyResponse")
  }
}