package no.ntnu.httpmock

import scala.collection.mutable.HashMap

import Types.{MockRequest, MockResponse}

class SimpleMockHandler extends MockHandler {
  val map = new HashMap[MockRequest, MockResponse]
  
  def getResponseFor(request: MockRequest): Option[MockResponse] = {
    map.get(request)
  }

  def registerMock(request: MockRequest, response: MockResponse): Unit = { 
    map.update(request, response)    
  }
  
  def getMockMap(): Map[MockRequest, MockResponse] = {
    map.toMap
  }
}