package no.ntnu.httpmock.matcher
import javax.servlet.http.HttpServletRequest

/** Build a stack of matchers. */
class MatcherBuilder {
  var matchers: List[RequestMatcher] = List()

  def withPath(path: String): MatcherBuilder = {
    matchers = matchers :+ new PathMatcher(path)
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
