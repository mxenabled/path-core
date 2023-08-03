package com.mx.path.core.context

import spock.lang.Specification

class UpstreamRequestProcessorTest extends Specification {
  def "builder"() {
    when:
    def subject = UpstreamRequestProcessor.builder().before({}).after({}).build()

    then:
    subject.getBefore() != null
    subject.getAfter() != null
  }

  def "executeBefore"() {
    given:
    def beforeCalled = false

    def subject = UpstreamRequestProcessor.builder().before({ request, response ->
      beforeCalled = true
    }).build()

    when:
    subject.executeBefore(null, null)
    subject.executeAfter(null, null)

    then:
    beforeCalled
  }

  def "executeAfter"() {
    given:
    def afterCalled = false

    def subject = UpstreamRequestProcessor.builder().after({ request, response ->
      afterCalled = true
    }).build()

    when:
    subject.executeBefore(null, null)
    subject.executeAfter(null, null)

    then:
    afterCalled
  }
}
