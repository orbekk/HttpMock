package no.ntnu.httpmock.matcher

import com.orbekk.logging.Logger
import javax.servlet.http.HttpServletRequest

object MatcherBuilder {
  def apply() = new MatcherBuilder
}

/** Build a stack of matchers. */
class MatcherBuilder extends Logger {
  var matchers: List[RequestMatcher] = List()

  def withPath(path: String) = {
    matchers = matchers :+ new PathMatcher(path)
    this
  }

  def withParameters(parameters: Map[String, Seq[String]]) = {
    matchers = matchers :+ new ParameterMatcher(parameters)
    this
  }

  private def internalBuild(matchers: List[RequestMatcher]): RequestMatcher =
    new RequestMatcher {
      override def matches(request: HttpServletRequest): Boolean = {
        matchers forall (_ matches request)
      }
    }

  def build(): RequestMatcher =
    internalBuild(matchers)
}
