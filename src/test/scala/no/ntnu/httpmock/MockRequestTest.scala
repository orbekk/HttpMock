package no.ntnu.httpmock

import java.io.StringReader
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MockRequestTest extends FunSuite {
  def parse(s: String): MockRequest = {
    MockRequest.parseFromRequest(new StringReader(s))
  }

  test("Invalid request should fail to parse") {
    assert(parse("") === null)
    assert(parse("invalid request") === null)
    assert(parse("{ {:") === null)
  }

  test("Valid requests should parse correctly") {
    assert(parse("""{"path": "/my/path"}""") != null)
    assert(parse("""
        {"path": "/my/path",
         "parameters": {
           "key": ["val1", "val2"]
         }
        }
        """) != null)
  }
}
