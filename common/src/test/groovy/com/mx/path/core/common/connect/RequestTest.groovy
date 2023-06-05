package com.mx.path.core.common.connect

import java.time.Duration

import com.mx.path.core.common.collection.SingleValueMap
import com.mx.path.core.common.request.Feature
import com.mx.testing.connect.TestFilterA
import com.mx.testing.connect.TestRequest

import spock.lang.Specification

class RequestTest extends Specification {
  Request subject
  RequestFilter filterChain

  def setup() {
    filterChain = new TestFilterA()
    subject = new TestRequest(filterChain)
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
    int mutualAuthProviderHashcode() {
      return 0
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

  def "testSetConnectionSettings"() {
    given:
    def settings = new TestMutualAuthSettings()
    subject.withConnectionSettings(settings)

    expect:
    subject.getConnectionSettings() == settings
  }

  def "equals & hashCode"() {
    given:
    def request = new TestRequest(filterChain)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.ACCOUNTS)
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
    def requestWithSameProperties = new TestRequest(filterChain)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.ACCOUNTS)
        .withHeader("headerKey", "headerValue")
        .withPath("/some/path")
        .withQueryStringParams(new SingleValueMap<String, String>().tap {put("key", "value")})
        .withTimeOut(Duration.ofMillis(100))

    then:
    request.equals(requestWithSameProperties)
    request.hashCode() == requestWithSameProperties.hashCode()

    when: "request with different properties"
    def requestWithDifferentProperties = new TestRequest(filterChain)
        .withAccept("application/json")
        .withBaseUrl("https://example.com")
        .withContentType("application/json")
        .withFeature(Feature.TRANSFERS)
        .withHeader("headerKey", "headerValue2")
        .withPath("/some/other/path")
        .withQueryStringParams(new SingleValueMap<String, String>().tap {put("key", "value2")})
        .withTimeOut(Duration.ofMillis(100))

    then:
    !request.equals(requestWithDifferentProperties)
    request.hashCode() != requestWithDifferentProperties.hashCode()
  }
}
