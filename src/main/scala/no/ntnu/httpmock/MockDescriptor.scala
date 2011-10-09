package no.ntnu.httpmock

import java.io.Reader
import javax.servlet.http.HttpServletResponse
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonParser
import no.ntnu.httpmock.matcher.MatcherBuilder

object MockDescriptor {
  def parseFromRequest(s: Reader): MockDescriptor =
    try {
      val json = JsonParser.parse(s)
      implicit val formats = DefaultFormats
      json.extract[MockDescriptor]
    } catch {
      case e:JsonParser.ParseException => return null
    }
}

/**
 * A MockDescriptor is an object that can be serialized to and from a JSON string
 * using net.liftweb.json.
 *
 * Example of JSON string:
 *   {"path": "/some/path",
 *    "parameters": {"parameter1": ["val1", "val2"]}}
 */
case class MockDescriptor(
    path: String,
    parameters: Option[Map[String, List[String]]],
    response: ResponseDescriptor) {
  def buildMatcher() = {
    val builder = MatcherBuilder()
    builder.withPath(path)
    parameters foreach (builder.withParameters _)
    builder.build()
  }
}

/**
 * The response to a mock call.
 *
 * This class contains the content and all headers for a mock response. For a
 * particular request, a response can be populated from this class.
 */
case class ResponseDescriptor(
    content: String,
    headers: Option[Map[String, String]]) {

  /**
   * Populate a HttpServletResponse with data from this ResponseDescriptor.
   */
  def copyToResponse(response: HttpServletResponse) {
    // TODO: Implement this method.
  }
}
