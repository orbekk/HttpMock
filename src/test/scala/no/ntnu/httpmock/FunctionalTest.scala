package no.ntnu.httpmock

import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Matchers.{anyInt, anyString}
import org.mockito.Mockito.{mock, when, verify, never}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.immutable.ListMap

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

  def setRequestBody(request: HttpServletRequest, body: String) {
    val reader = new BufferedReader(new StringReader(body))
    when(request.getReader()) thenReturn reader
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
    // TODO: This can be replaced with JSON serialized from a MockRequest,
    // which would make it easier to handle than a raw string.
    setRequestBody(controllerRequest, 
      """{"path": "/testpath",
          "parameters": {
            "SomeParameter": ["Value"]}}""")
    when(controllerRequest.getMethod()) thenReturn "POST"
    controller.service(controllerRequest, controllerResponse)
    checkSuccess(controllerResponse)

    val request = mock(classOf[HttpServletRequest])
    when(request.getMethod()) thenReturn "GET"
    when(request.getRequestURI()) thenReturn "/testpath"
    setRequestParameters(request, "SomeParameter" -> Array("Value"))

    val response = mock(classOf[HttpServletResponse])
    val writer = new StringWriter()
    when(response.getWriter()) thenReturn (new PrintWriter(writer))

    mockServlet.service(request, response)
    checkSuccess(response)
    // TODO: When mock responses has been implemented, verify the response:
    // assert(writer.toString() === "MyResponse")
  }
}
