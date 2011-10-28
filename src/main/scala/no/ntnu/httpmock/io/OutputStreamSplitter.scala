package no.ntnu.httpmock.io

import java.lang.StringBuilder
import javax.servlet.ServletOutputStream

class OutputStreamSplitter(servletStream: ServletOutputStream)
    extends ServletOutputStream {
  val stringBuilder = new StringBuilder

  def write(b: Int) {
    stringBuilder.append(b.toChar)
    servletStream.write(b)
  }

  def getContent(): String = stringBuilder.toString()
}
