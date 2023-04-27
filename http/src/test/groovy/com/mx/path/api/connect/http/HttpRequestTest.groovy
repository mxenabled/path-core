package com.mx.path.api.connect.http

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify

import com.mx.path.connect.http.HttpRequest
import com.mx.path.connect.http.HttpResponse
import com.mx.path.core.common.connect.RequestFilter

import spock.lang.Specification
import testing.connect.RequestFilterStub

class HttpRequestTest extends Specification {
  HttpRequest subject
  RequestFilter filterChain

  def setup() {
    filterChain = spy(new RequestFilterStub())
    subject = new HttpRequest(filterChain)
  }

  def "execute"() {
    when:
    subject.execute()

    then:
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }

  def "get"() {
    when:
    subject.get()

    then: "sets method and executes"
    subject.getMethod() == "GET"
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }

  def "test put"() {
    when:
    subject.put()

    then:"sets method and executes"
    subject.getMethod() == "PUT"
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }

  def "test delete"() {
    when:
    subject.delete()

    then:"sets method and executes"
    subject.getMethod() == "DELETE"
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }

  def "test patch"() {
    when:
    subject.patch()

    then:"sets method and executes"
    subject.getMethod() == "PATCH"
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }

  def "test post"() {
    when:
    subject.post()

    then:"sets method and executes"
    subject.getMethod() == "POST"
    verify(filterChain).execute(any(HttpRequest), any(HttpResponse))
  }
}
