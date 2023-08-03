package com.mx.path.core.context

import spock.lang.Specification

class RequestContextTest extends Specification {

  def cleanup() {
    RequestContext.clear()
  }

  def "current"() {
    given:
    def context = RequestContext.builder().build()

    when:
    context.register()

    then:
    RequestContext.current() == context
  }

  def "reset"() {
    given:
    def context = RequestContext.builder().build()

    when:
    context.register()

    then:
    RequestContext.current() == context

    when:
    RequestContext.clear()

    then:
    RequestContext.current() == null
  }

  def "builder"() {
    when:
    def subject = RequestContext.builder()
        .clientGuid("clientAF")
        .clientId("client1")
        .feature("featureA")
        .header("h2" , "v2")
        .originatingIP("127.0.0.1")
        .parameter("p2", "v2")
        .path("/accounts")
        .sessionTraceId("abcde")
        .userGuid("userAF")
        .withHeaders({ h -> h.put("h1", "v1") })
        .withParameters({ p -> p.put("p1", "v1") })
        .build()

    then:
    subject.getClientGuid() == "clientAF"
    subject.getClientId() == "client1"
    subject.getFeature() == "featureA"
    subject.getOriginatingIP() == "127.0.0.1"
    subject.getPath() == "/accounts"
    subject.getSessionTraceId() == "abcde"
    subject.getUserGuid() == "userAF"
    subject.getHeaders().get("h2") == "v2"
    subject.getHeaders().get("h1") == "v1"
    subject.getParams().get("p1") == "v1"
    subject.getParams().get("p2") == "v2"

    and: "UpstreamRequestConfiguration is initialized"
    subject.getUpstreamRequestConfiguration() != null
  }

  def "withSelfClearing"() {
    given:
    def context = RequestContext.builder().build()

    when: "context already set"
    context.register()

    RequestContext.withSelfClearing("client1", { c ->
      assert context == c, "Must pass current() context"
    })

    then:
    true

    when:
    RequestContext.clear()

    RequestContext.withSelfClearing("client1", { c ->
      assert c.getClientId() == "client1", "Client ID not set"
      assert context != c, "Must set new current() context"
    })

    then:
    true
  }

  def "setters"() {
    given:
    def subject = RequestContext.builder().build()
    def upstreamRequestConfiguration = new UpstreamRequestConfiguration()

    when:
    subject.setClientGuid("clientAF")
    subject.setClientId("client1")
    subject.setFeature("featureA")
    subject.setOriginatingIP("127.0.0.1")
    subject.setPath("/accounts")
    subject.setSessionTraceId("abcde")
    subject.setUpstreamRequestConfiguration(upstreamRequestConfiguration)
    subject.setUserGuid("userAF")
    subject.getHeaders().put("h2", "v2")
    subject.getHeaders().put("h1", "v1")
    subject.getParams().put("p1", "v1")
    subject.getParams().put("p2", "v2")

    then:
    subject.getClientGuid() == "clientAF"
    subject.getClientId() == "client1"
    subject.getFeature() == "featureA"
    subject.getOriginatingIP() == "127.0.0.1"
    subject.getPath() == "/accounts"
    subject.getSessionTraceId() == "abcde"
    subject.getUpstreamRequestConfiguration() == upstreamRequestConfiguration
    subject.getUserGuid() == "userAF"
    subject.getHeaders().get("h2") == "v2"
    subject.getHeaders().get("h1") == "v1"
    subject.getParams().get("p1") == "v1"
    subject.getParams().get("p2") == "v2"
  }

  def "toBuilder"() {
    given:
    def subject = RequestContext.builder().build()
    def upstreamRequestConfiguration = new UpstreamRequestConfiguration()

    when:
    subject.setClientGuid("clientAF")
    subject.setClientId("client1")
    subject.setFeature("featureA")
    subject.setOriginatingIP("127.0.0.1")
    subject.setPath("/accounts")
    subject.setSessionTraceId("abcde")
    subject.setUpstreamRequestConfiguration(upstreamRequestConfiguration)
    subject.setUserGuid("userAF")
    subject.getHeaders().put("h2", "v2")
    subject.getHeaders().put("h1", "v1")
    subject.getParams().put("p1", "v1")
    subject.getParams().put("p2", "v2")

    def result = subject.toBuilder().build()
    then:
    result.getClientGuid() == "clientAF"
    result.getClientId() == "client1"
    result.getFeature() == "featureA"
    result.getOriginatingIP() == "127.0.0.1"
    result.getPath() == "/accounts"
    result.getSessionTraceId() == "abcde"
    subject.getUpstreamRequestConfiguration() == upstreamRequestConfiguration
    result.getUserGuid() == "userAF"
    result.getHeaders().get("h2") == "v2"
    result.getHeaders().get("h1") == "v1"
    result.getParams().get("p1") == "v1"
    result.getParams().get("p2") == "v2"
  }
}
