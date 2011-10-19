package no.ntnu.httpmock.servlet

import org.junit.runner.RunWith
import org.mockito.Mockito.{mock, verify, never}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunctionalTest extends FunSuite with BeforeAndAfterEach {
  var contextRequestLogger: LoggerWrapperServlet.ContextRequestLogger = null
  var mockLogger: LoggerWrapperServlet.RequestLogger = null

  override def beforeEach() {
    mockLogger = mock(classOf[LoggerWrapperServlet.RequestLogger])
    contextRequestLogger = new LoggerWrapperServlet.ContextRequestLogger(mockLogger)
  }

  test("Logs in a simple context") {
    contextRequestLogger.withScope("my scope") { logger =>
      logger.log("test tag", null, null) 
    }
    verify(mockLogger).log("test tag", null, null)
  }

  test("Nesting logging only logs innermost request") {
    contextRequestLogger.withScope("first scope") { logger1 =>
      contextRequestLogger.withScope("second scope") { logger2 =>
        logger2.log("second scope", null, null)
      }
      logger1.log("first scope", null, null)
    }
    verify(mockLogger).log("second scope", null, null)
    verify(mockLogger, never()).log("first scope", null, null)
  }
}
