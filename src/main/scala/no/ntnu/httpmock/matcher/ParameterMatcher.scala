package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.mapAsScalaMap

object ParameterMatcher {
  // TODO: Add partial matching.
  case class Options(ignoredFields: List[String])
  val defaultOptions = Options(Nil)
}

/**
 * A class that matches HTTP key-value parameters.
 */
class ParameterMatcher(parameters: Map[String, Array[String]],
    options: ParameterMatcher.Options = ParameterMatcher.defaultOptions)
    extends Matcher {

  protected[httpmock] def internalMatches(
      mockParameters: Map[String, Array[String]],
      requestParameters: Map[String, Array[String]]): Boolean = {
    val parameters0 = mockParameters-- options.ignoredFields
    val requestParameters0 = requestParameters -- options.ignoredFields
    parameters0 == requestParameters0
  }

  def matches(request: HttpServletRequest): Boolean = {
    val requestParameters = request.getParameterMap().toMap
    internalMatches(parameters, requestParameters)
  }
}