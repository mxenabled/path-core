package com.mx.path.gateway.net

import static org.mockito.Mockito.mock

import spock.lang.Specification

class RequestProcessorFactoryTest extends Specification {
  Request request
  Request altRequest
  RequestProcessor processor
  RequestProcessor altProcessor

  def setup() {
    request = new Request()
        .withBaseUrl("http://localhost:999/testingPath")
        .withFeatureName("unitTest")

    altRequest = new Request()
        .withBaseUrl("http://localhost:999/altTestingPath")
        .withFeatureName("unitTest")

    processor = mock(RequestProcessor)
    altProcessor = mock(RequestProcessor)
  }

  def cleanup() {
    RequestProcessorFactory.resetProcessors()
  }

  def "setProcessorForRequest"() {
    when:
    RequestProcessorFactory.setProcessorForRequest(request, processor)
    RequestProcessorFactory.setProcessorForRequest(altRequest, altProcessor)

    then:
    RequestProcessorFactory.forRequest(request) == processor
    RequestProcessorFactory.forRequest(altRequest) == altProcessor
  }

  def "resetProcessors"() {
    when:
    RequestProcessorFactory.setProcessorForRequest(request, processor)
    RequestProcessorFactory.resetProcessors()

    then:
    HttpClientRequestProcessor == RequestProcessorFactory.forRequest(request).class
  }
}
