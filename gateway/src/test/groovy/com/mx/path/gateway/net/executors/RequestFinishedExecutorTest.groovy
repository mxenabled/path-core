package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response

import spock.lang.Specification

class RequestFinishedExecutorTest extends Specification {
  Request request
  Response response
  RequestExecutor next
  RequestFinishedExecutor subject

  def setup() {
    request = new Request()
    response = mock(Response.class)
    next = mock(RequestExecutor.class)
  }

  def "calls response finish"() {
    given:
    subject = new RequestFinishedExecutor(next)

    when:
    subject.execute(request, response)
    verify(response).finish()
    verify(next).execute(request, response)

    then:
    true
  }
}
