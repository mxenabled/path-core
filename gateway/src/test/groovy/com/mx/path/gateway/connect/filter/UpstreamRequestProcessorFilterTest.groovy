package com.mx.path.gateway.connect.filter

import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.UpstreamRequestConfiguration
import com.mx.path.core.context.UpstreamRequestProcessor

import spock.lang.Specification

class UpstreamRequestProcessorFilterTest extends Specification {
  UpstreamRequestProcessorFilter subject

  def setup() {
    subject = new UpstreamRequestProcessorFilter()
  }

  def "executes before and after"() {
    given:
    def beforeCalled = false
    def afterCalled = false

    RequestContext.builder()
        .upstreamRequestConfiguration(
        UpstreamRequestConfiguration.builder()
        .upstreamRequestProcessor(UpstreamRequestProcessor.builder()
        .before({ request, response -> beforeCalled = true})
        .after({ request, response -> afterCalled = true})
        .build()
        ).build()
        )
        .build()
        .register()

    when:
    subject.execute(null, null)

    then:
    beforeCalled
    afterCalled
  }
}
