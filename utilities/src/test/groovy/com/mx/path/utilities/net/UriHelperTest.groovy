package com.mx.path.utilities.net

import spock.lang.Specification

class UriHelperTest extends Specification {
  def "appends a query to a URI (no existing params)"() {
    given:
    def subject = new UriHelper("http://example.com/")

    when:
    def result = subject.appendQueryParameter("myParam", "true")
        .appendQueryParameter("otherParam", "123")
        .appendQueryParameter("thirdParam", "foo")

    then:
    result.toString() == "http://example.com/?myParam=true&otherParam=123&thirdParam=foo"
  }

  def "appends a query to a URI (existing params)"() {
    given:
    def subject = new UriHelper("http://example.com/?myParam=true")

    when:
    def result = subject.appendQueryParameter("otherParam", "123").appendQueryParameter("thirdParam", "foo")

    then:
    result.toString() == "http://example.com/?myParam=true&otherParam=123&thirdParam=foo"
  }

  def "clear query parameters"() {
    given:
    def subject = new UriHelper("http://example.com/?myParam=true&otherParam=123&thirdParam=foo")

    when:
    def result = subject.clearQueryParameters()

    then:
    result.toString() == "http://example.com/"
  }

  def "throws an exception on malformed URIs"() {
    when:
    new UriHelper("the spice must flow")

    then:
    thrown(URISyntaxException)
  }
}
