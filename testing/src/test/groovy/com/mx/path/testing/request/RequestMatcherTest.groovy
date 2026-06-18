package com.mx.path.testing.request

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import com.mx.path.core.common.connect.Request
import com.mx.path.testing.request.RequestMatcher

import spock.lang.Specification

class RequestMatcherTest extends Specification {

  def makeRequest(String path, String method = "GET", String baseUrl = "http://example.com") {
    def req = mock(Request)
    when(req.getPath()).thenReturn(path)
    when(req.getMethod()).thenReturn(method)
    when(req.getBaseUrl()).thenReturn(baseUrl)
    return req
  }

  def "withPath matches correct path"() {
    given:
    def matcher = RequestMatcher.Fluent.withPath("accounts")

    expect:
    matcher.isMatch(makeRequest("accounts"))
    !matcher.isMatch(makeRequest("transactions"))
  }

  def "withMethod matches correct method"() {
    given:
    def matcher = RequestMatcher.Fluent.withMethod("POST")

    expect:
    matcher.isMatch(makeRequest("accounts", "POST"))
    !matcher.isMatch(makeRequest("accounts", "GET"))
  }

  def "withPath and withMethod combined — both must match"() {
    given:
    def matcher = RequestMatcher.Fluent.withPath("accounts").withMethod("GET")

    expect:
    matcher.isMatch(makeRequest("accounts", "GET"))
    !matcher.isMatch(makeRequest("accounts", "POST"))
    !matcher.isMatch(makeRequest("transactions", "GET"))
  }

  def "withBaseUrl matches correct base URL"() {
    given:
    def matcher = new RequestMatcher().withBaseUrl("http://example.com")

    expect:
    matcher.isMatch(makeRequest("accounts"))
    !matcher.isMatch(makeRequest("accounts", "GET", "http://other.com"))
  }

  def "with uses provided function matcher"() {
    given:
    def matcher = new RequestMatcher().with({ req -> req.getPath() == "accounts" } as java.util.function.Function)

    expect:
    matcher.isMatch(makeRequest("accounts"))
    !matcher.isMatch(makeRequest("other"))
  }

  def "withMatcher uses provided function matcher"() {
    given:
    def matcher = new RequestMatcher().withMatcher({ req -> req.getMethod() == "DELETE" } as java.util.function.Function)

    expect:
    matcher.isMatch(makeRequest("any", "DELETE"))
    !matcher.isMatch(makeRequest("any", "GET"))
  }

  def "Fluent.with uses provided function"() {
    given:
    def matcher = RequestMatcher.Fluent.with({ req -> req.getPath() == "accounts" } as java.util.function.Function)

    expect:
    matcher.isMatch(makeRequest("accounts"))
  }

  def "Fluent.withBaseUrl matches base URL"() {
    given:
    def matcher = RequestMatcher.Fluent.withBaseUrl("http://example.com")

    expect:
    matcher.isMatch(makeRequest("accounts"))
  }

  def "isMatch returns true when no assertions defined"() {
    expect:
    new RequestMatcher().isMatch(makeRequest("anything"))
  }

  def "describe returns non-null string"() {
    given:
    def matcher = RequestMatcher.Fluent.withPath("accounts").withMethod("GET")

    expect:
    matcher.describe() != null
  }
}
