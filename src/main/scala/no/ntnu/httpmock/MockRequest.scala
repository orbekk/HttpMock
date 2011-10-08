package no.ntnu.httpmock

import java.io.Reader
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonParser
import no.ntnu.httpmock.matcher.MatcherBuilder

object MockRequest {
  def parseFromRequest(s: Reader): MockRequest =
    try {
      val json = JsonParser.parse(s)
      implicit val formats = DefaultFormats
      json.extract[MockRequest]
    } catch {
      case e:JsonParser.ParseException => return null
    }
}

/**
 * A MockRequest is an object that can be serialized to and from a JSON string
 * using net.liftweb.json.
 *
 * Example of JSON string:
 *   {"path": "/some/path",
 *    "parameters": {"parameter1": ["val1", "val2"]}}
 */
case class MockRequest(
    path: String,
    parameters: Option[Map[String, List[String]]]) {
  def buildMatcher() = {
    val builder = MatcherBuilder()
    builder.withPath(path)
    parameters foreach (builder.withParameters _)
    builder.build()
  }
}
