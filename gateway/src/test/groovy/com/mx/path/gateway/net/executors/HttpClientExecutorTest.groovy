package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.*

import com.mx.common.collections.MultiValueMap
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response

import org.apache.http.HttpEntity

import spock.lang.Specification
import spock.lang.Unroll

class HttpClientExecutorTest extends Specification {
  static MultiValueMap<String, String> makeContentTypeHeaders(String... contentTypes) {
    MultiValueMap<String, String> headers = new MultiValueMap<>()
    headers.addAll("Content-Type", Arrays.asList(contentTypes))
    return headers
  }

  def "sets the Response body based on the Request's preferences"() {
    given:
    def subject = spy(new HttpClientExecutor())
    def request = new Request()
    def response = new Response()
    MultiValueMap<String, String> responseHeaders = new MultiValueMap<>()
    def httpEntity = mock(HttpEntity)
    def stringBody = "This is the response"
    def rawBody = stringBody.bytes

    doReturn(stringBody).when(subject).getHttpEntityBodyAsString(httpEntity)
    doReturn(rawBody).when(subject).getHttpEntityBodyAsBytes(httpEntity)

    when: "No preference is specified"
    subject.setResponseBody(request, response, responseHeaders, httpEntity)

    then:
    response.body == stringBody
    response.rawBody.length == 0

    when: "Raw body is preferred"
    request = new Request().tap {
      withPreferredResponseBodyType(Request.PreferredResponseBodyType.RAW)
    }
    response = new Response()
    subject.setResponseBody(request, response, responseHeaders, httpEntity)

    then:
    response.body == ""
    response.rawBody == rawBody


    when: "String body is preferred"
    request = new Request().tap {
      withPreferredResponseBodyType(Request.PreferredResponseBodyType.STRING)
    }
    response = new Response()
    subject.setResponseBody(request, response, responseHeaders, httpEntity)

    then:
    response.body == stringBody
    response.rawBody.length == 0


    when: "Both String and raw body are preferred"
    request = new Request().tap {
      withPreferredResponseBodyType(Request.PreferredResponseBodyType.STRING_AND_RAW)
    }
    response = new Response()
    subject.setResponseBody(request, response, responseHeaders, httpEntity)

    then:
    response.body == stringBody
    response.rawBody == rawBody
  }

  @Unroll
  def "infers whether the #rawBodyShouldBeReturned based on the #contentTypeHeaders"() {
    given:
    def subject = new HttpClientExecutor()

    when:
    def result = subject.shouldReturnRawBody(contentTypeHeaders)

    then:
    result == rawBodyShouldBeReturned

    where:
    rawBodyShouldBeReturned || contentTypeHeaders
    true                    || makeContentTypeHeaders("application/pdf")
    true                    || makeContentTypeHeaders("image/png")
    true                    || makeContentTypeHeaders("IMAGE/JPEG")
    true                    || makeContentTypeHeaders("Image/gif")
    true                    || makeContentTypeHeaders("image/svg+xml")
    true                    || makeContentTypeHeaders("image/tiff")
    true                    || makeContentTypeHeaders("application/msword")
    false                   || makeContentTypeHeaders("application/json")
    false                   || makeContentTypeHeaders("text/html")
    false                   || makeContentTypeHeaders("application/xml")
    false                   || makeContentTypeHeaders()
  }
}
