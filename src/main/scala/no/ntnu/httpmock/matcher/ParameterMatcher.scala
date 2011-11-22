package no.ntnu.httpmock.matcher

import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.WrappedArray
import com.orbekk.logging.Logger

object ParameterMatcher {
  // TODO: Add partial matching.
  case class Options(ignoredFields: List[String], partialMatching: Boolean)
  val defaultOptions = Options(ignoredFields = List(), partialMatching = true)

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
          val sizeCheck = map.size == that0.map.size || options.partialMatching
          val result = sizeCheck && map.forall {
            case (k, v) => that0.map.get(k) match {
              case Some(v0) => seqCompare(v, v0)
              case None => false
            }
          }
          logger.info("Testing parameters: \n  " + parameters.toString() + "\n  " +
              that0.toString() + "\n  => " + result)
          result
        case _ => false
      }
    }

    def seqCompare[T](array1: Seq[T], array2: Seq[T]): Boolean = {
      logger.info("Sequences equel? " + (array1 == array2) +
          "\n  " + array1 + "\n  " + array2)
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
    type J = java.util.Map[String, Array[String]]
    val javaRequestParameters: J =
      request.getParameterMap().asInstanceOf[J]
    val requestParameters: Map[String, Seq[String]] =
      ParameterMatcher.convertParameterMap(javaRequestParameters.toMap)
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
