package com.mx.path.core.context

import spock.lang.Specification

class UpstreamRequestConfigurationTest extends Specification {
  def "constructor"() {
    when:
    def subject = new UpstreamRequestConfiguration()

    then:
    subject.getForwardedRequestHeaders().isEmpty()
    subject.getUpstreamRequestProcessors().isEmpty()
  }

  def "builder"() {
    when: "take defaults"
    def subject = UpstreamRequestConfiguration.builder().build()

    then: "collections are initialized"
    subject.getForwardedRequestHeaders().isEmpty()
    subject.getUpstreamRequestProcessors().isEmpty()

    when:
    def upstreamRequestProcessors = UpstreamRequestProcessor.builder().build()
    subject = UpstreamRequestConfiguration.builder()
        .forwardedHeader("h1", "v1")
        .upstreamRequestProcessor(upstreamRequestProcessors)
        .build()

    then:
    subject.getForwardedRequestHeaders().get("h1") == "v1"
    subject.getUpstreamRequestProcessors() == [upstreamRequestProcessors]
  }
}
