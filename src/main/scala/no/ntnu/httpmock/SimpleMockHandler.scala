package no.ntnu.httpmock

import scala.collection.mutable.HashMap

class SimpleMockHandler extends MockHandler {
  val map = new HashMap[Types.MockRequest, Types.MockResponse]
  
  def getResponseFor(request: Types.MockRequest): Option[Types.MockResponse] = {
    map.get(request)
  }

  def registerMock(request: Types.MockRequest, response: Types.MockResponse) = {
    map.update(request, response)    
  }
  
  def getMockMap(): Map[Types.MockRequest, Types.MockResponse] = {
    map.toMap
  }
}