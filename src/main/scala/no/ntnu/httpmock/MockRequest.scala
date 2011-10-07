package no.ntnu.httpmock

import java.io.Reader
import net.liftweb.json.JsonParser
import net.liftweb.json.DefaultFormats

object MockRequest {
  def parseFromRequest(s: Reader) {
    try {
      val json = JsonParser.parse(s)
      implicit val formats = DefaultFormats
      json.extract[MockRequest]
    } catch {
      case e:JsonParser.ParseException => return null
    }
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
    parameters: Map[String, List[String]]
)
