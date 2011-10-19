package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.WrappedArray
import com.orbekk.logging.Logger

object ParameterMatcher {
  // TODO: Add partial matching.
  case class Options(ignoredFields: List[String])
  val defaultOptions = Options(Nil)

  def fromJavaParameterMap(parameters: Map[String, Array[String]]) =
    new ParameterMatcher(convertParameterMap(parameters))

  def convertParameterMap(parameters: Map[String, Array[String]]):
      Map[String, Seq[String]]  = {
        def convert(x: Array[String]): Seq[String] = x  // Uses WrappedArray
        parameters mapValues (x => x)
  }
}

/**
 * A class that matches HTTP key-value parameters.
 */
class ParameterMatcher(parameters: Map[String, Seq[String]],
    options: ParameterMatcher.Options = ParameterMatcher.defaultOptions)
    extends RequestMatcher with Logger {

  /** Wraps a parameter map and implements equivalence checking. */
  private case class ParameterMap(val map: Map[String, Seq[String]]) {
    override def equals(that: Any): Boolean = {
      that match {
        case that0: ParameterMap =>
            map.size == that0.map.size && map.forall {
              case (k, v) => that0.map.get(k) match {
              case Some(v0) => seqCompare(v, v0)
              case None => false
            }
          }
        case _ => false
      }
    }

    def seqCompare[T](array1: Seq[T], array2: Seq[T]): Boolean = {
      array1 == array2
    }
  }

  private[matcher] def internalMatches(
      mockParameters: Map[String, Seq[String]],
      requestParameters: Map[String, Seq[String]]): Boolean = {
    val parameters0 = ParameterMap(mockParameters -- options.ignoredFields)
    val requestParameters0 = ParameterMap(requestParameters -- options.ignoredFields)
    parameters0 == requestParameters0
  }

  def matches(request: HttpServletRequest): Boolean = {
    // For some reason the compiler refuses to infer the generic types
    // and returns a Map[_, _]. This cast is necessary.
    val javaRequestParameters: Map[String, Array[String]]=
      request.getParameterMap().asInstanceOf[Map[String,Array[String]]]
    val requestParameters: Map[String, Seq[String]] =
      ParameterMatcher.convertParameterMap(javaRequestParameters)
    internalMatches(parameters, requestParameters)
  }

  def parameterMapToString(map: Map[String, Seq[String]]): String = {
    var result = new StringBuilder
    for ((param, values) <- map) {
      result.append(param + ": ")
      values.addString(result, ", ")
      result.append("\n")
    }
    result.toString
  }
}
