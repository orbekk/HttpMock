package no.ntnu.httpmock

import java.io.StringReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import net.liftweb.json.Serialization
import net.liftweb.json.DefaultFormats

@RunWith(classOf[JUnitRunner])
class MockDescriptorTest extends FunSuite {
  def parse(s: String): MockDescriptor = {
    MockDescriptor.parseFromRequest(new StringReader(s))
  }

  test("Invalid request should fail to parse") {
    assert(parse("") === null)
    assert(parse("invalid request") === null)
    assert(parse("{ {:") === null)
  }

  test("Valid requests should parse correctly") {
    assert(parse("""
        {"path": "/my/path",
         "parameters": {
           "key": ["val1", "val2"]
         },
         "response": {
           "content": "the content"
         }
        }
        """) != null)
  }
}
