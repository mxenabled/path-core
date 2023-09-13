package com.mx.path.core.common.connect

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

import java.time.Duration

import com.github.rholder.retry.RetryerBuilder
import com.github.rholder.retry.StopStrategies
import com.mx.path.core.common.collection.SingleValueMap
import com.mx.path.core.common.request.Feature
import com.mx.testing.connect.TestFilterA
import com.mx.testing.connect.TestRequest
import com.mx.testing.connect.TestResponse

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

  def "start"() {
    when:
    filterChain = mock(RequestFilter)
    subject = new TestRequest(filterChain)

    then:
    subject.getAttemptCount() == 0

    when:
    subject.start()

    then:
    subject.getAttemptCount() == 1

    when:
    subject.start()

    then: // Does not increment
    subject.getAttemptCount() == 1
  }

  def "startRetry"() {
    when:
    filterChain = mock(RequestFilter)
    subject = new TestRequest(filterChain)

    then:
    subject.getAttemptCount() == 0

    when:
    subject.start()

    then:
    subject.getAttemptCount() == 1

    when:
    subject.startRetry()

    then: // Does not increment
    subject.getAttemptCount() == 2
  }

  def "execute"() {
    given:
    filterChain = mock(RequestFilter)
    subject = new TestRequest(filterChain)

    when:
    subject.execute()

    then:
    verify(filterChain, times(1)).execute(any(), any())
  }

  def "execute with retryer"() {
    given:
    filterChain = mock(RequestFilter)
    subject = new TestRequest(filterChain)
    subject.withRetryer(RetryerBuilder.<TestResponse>newBuilder()
        .retryIfResult({response -> true }) // always retry
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        .build())

    when:
    subject.execute()

    then:
    verify(filterChain, times(3)).execute(any(), any())
  }

  def "execute with retryer from configuration node"() {
    given:
    filterChain = mock(RequestFilter)
    subject = new TestRequest(filterChain)
    subject.withRetryConfiguration(RetryConfiguration.builder()
        .stopStrategy(RetryConfiguration.StopStrategy.COUNT)
        .count(3)
        .retryOn(Collections.singletonList(ResponseMatcher.builder().predicate({ t -> true }).build()))
        .build())

    when:
    subject.execute()

    then:
    verify(filterChain, times(3)).execute(any(), any())
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
