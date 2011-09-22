package no.ntnu.httpmock
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest

object ServletHelper {
  class ParameterNotFoundException(message: String) extends Exception(message)
  
  /** Get the request path relative to the servlet path.
   * 
   * Strips the servlet path from the request URI. Example: If the servlet path
   * is "/MyServlet", and the request path is "/MyServlet/SomePage", returns
   * "/SomePage".
   */
  def getServletRelativePath(request: HttpServletRequest): String =
    request.getRequestURI().replace(request.getServletPath(), "")
    
  /** Get a request parameter or fail with an exception */
  @throws(classOf[ParameterNotFoundException])
  def getParameter(parameterName: String, request: HttpServletRequest): String = {
    val value = request.getParameter(parameterName)
    if (value == null) {
      throw new ParameterNotFoundException(parameterName)
    }
    value
  }
}