package com.mx.path.gateway.net

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import java.time.Duration

import com.google.common.collect.LinkedListMultimap
import com.mx.common.collections.SingleValueMap
import com.mx.common.connect.ConnectionSettings
import com.mx.common.connect.RequestFilter
import com.mx.common.request.Feature

import io.opentracing.Span
import io.opentracing.SpanContext
import io.opentracing.mock.MockTracer

import spock.lang.Specification


class RequestTest extends Specification {
  Request subject

  def setup() {
    subject = new Request()
  }

  def "testAcceptStringSetsHeader"() {
    given:
    subject.withAccept("application/vnd.moneydesktop.mdx.v5+xml")

    expect:
    subject.getAccept() == "application/vnd.moneydesktop.mdx.v5+xml"
    subject.getHeaders().get("Accept") == "application/vnd.moneydesktop.mdx.v5+xml"
  }

  def "testBaseUrl"() {
    given:
    subject.withBaseUrl("http://base")

    expect:
    subject.getBaseUrl() == "http://base"
  }

  def "testBody"() {
    given:
    subject.withBody("{}")

    expect:
    subject.getBody() == "{}"
  }

  def "testBodyJson"() {
    given:
    subject.withBodyJson("{}")

    expect:
    subject.getBodyJson() == "{}"
  }

  def "testContentTypeStringSetsHeader"() {
    given:
    subject.withContentType("application/vnd.moneydesktop.mdx.v5+xml")

    expect:
    subject.getContentType() == "application/vnd.moneydesktop.mdx.v5+xml"
    subject.getHeaders().get("Content-Type") == "application/vnd.moneydesktop.mdx.v5+xml"
  }

  def "testPath"() {
    given:
    subject.withPath("/path")

    expect:
    subject.getPath() == "/path"
  }

  def "testRequestUrl"() {
    given:
    subject.withBaseUrl("http://base").withPath("/path")

    expect:
    subject.getUri() == "http://base/path"
  }

  def "testHeader"() {
    given:
    subject.getHeaders().clear()
    subject.withHeader("key", "value")

    expect:
    subject.getHeaders().size() == 1
    subject.getHeaders().get("key") == "value"
  }

  def "testHeaders"() {
    given:
    subject.getHeaders().clear()
    subject.withHeaders({ headers ->
      headers.put("key", "value")
    })

    expect:
    subject.getHeaders().get("key") == "value"
    subject.getHeaders().size() == 1
  }

  def "testMethod"() {
    given:
    subject.withMethod("PUT")

    expect:
    subject.getMethod() == "PUT"
  }

  def "testQueryStringParamsConsumer"() {
    given:
    subject.withQueryStringParams({ parameters ->
      parameters.put("q", "find this")
    })

    expect:
    subject.getQueryStringParams().get("q") == "find this"
    subject.getQueryStringParams().size() == 1
  }

  def "testQueryStringParams"() {
    given:
    def params = new SingleValueMap<String, String>()
    params.put("q1", "v1")
    subject.withQueryStringParams(params)

    expect:
    subject.getQueryStringParams().get("q1") == "v1"
    subject.getQueryStringParams().size() == 1
  }

  class TestMutualAuthSettings implements ConnectionSettings {
    @Override
    String getKeystorePath() {
      return null
    }

    @Override
    char[] getKeystorePassword() {
      return null
    }

    @Override
    List<RequestFilter> getBaseRequestFilters() {
      return null
    }

    @Override
    boolean getSkipHostNameVerify() {
      return false
    }

    @Override
    String getBaseUrl() {
      return null
    }

    @Override
    String getCertificateAlias() {
      return null
    }
  }

  def "testSetMutualAuthenticationSettings"() {
    given:
    def settings = new TestMutualAuthSettings()
    subject.withMutualAuthSettings(settings)

    expect:
    subject.getMutualAuthSettings() == settings
  }

  def "trace Id"() {
    given:
    def span = mock(Span.class)
    def context = mock(SpanContext.class)
    when(span.context()).thenReturn(context)

    when:
    when(context.toTraceId()).thenReturn("1")
    subject.withSpanContext(context)

    then:
    subject.getTraceId() == "1"
  }

  def "span Id"() {
    given:
    def span = mock(Span.class)
    def context = mock(SpanContext.class)
    when(span.context()).thenReturn(context)

    when:
    when(context.toSpanId()).thenReturn("1")
    subject.withSpanContext(context)

    then:
    subject.getTraceSpanId() == "1"
  }

  def "equals & hashCode"() {
    given:
    def context = mock(SpanContext.class)
    when(context.toSpanId()).thenReturn("1")
    when(context.toTraceId()).thenReturn("2")
    when(context.baggageItems()).thenReturn(new HashMap<>().tap {put("parentId", "3")}.collect())

    def request = new Request()
        .withSpanContext(context)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.ACCOUNTS)
        .withFeatureName("featureName")
        .withHeader("headerKey", "headerValue")
        .withPath("/some/path")
        .withQueryStringParams(new SingleValueMap<String, String>().tap {put("key", "value")})
        .withTimeOut(Duration.ofMillis(100))

    when: "same instance"
    def sameRequestInstance = request

    then:
    request.equals(sameRequestInstance)
    request.hashCode() == sameRequestInstance.hashCode()

    when: "null"
    def nullRequest = null

    then:
    !request.equals(nullRequest)

    when: "request with same properties"
    def requestWithSameProperties = new Request()
        .withSpanContext(context)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.ACCOUNTS)
        .withFeatureName("featureName")
        .withHeader("headerKey", "headerValue")
        .withPath("/some/path")
        .withQueryStringParams(new SingleValueMap<String, String>().tap {put("key", "value")})
        .withTimeOut(Duration.ofMillis(100))

    then:
    request.equals(requestWithSameProperties)
    request.hashCode() == requestWithSameProperties.hashCode()

    when: "request with different properties"
    def requestWithDifferentProperties = new Request()
        .withSpanContext(context)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.TRANSFERS)
        .withFeatureName("featureName")
        .withHeader("headerKey", "headerValue2")
        .withPath("/some/other/path")
        .withQueryStringParams(new SingleValueMap<String, String>().tap {put("key", "value2")})
        .withTimeOut(Duration.ofMillis(100))

    then:
    !request.equals(requestWithDifferentProperties)
    request.hashCode() != requestWithDifferentProperties.hashCode()
  }
}
