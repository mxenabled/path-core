package com.mx.path.core.common.model

import com.mx.testing.TestAccount

import spock.lang.Specification

class ModelBaseTest extends Specification {
  def subject

  def setup() {
    subject = new TestAccount()
  }

  def "getObjectMetadata"() {
    when:
    def metadata = subject.getObjectMetadata()

    then:
    metadata != null
  }

  def "wrapped marks object as wrapped and returns self"() {
    when:
    def result = subject.wrapped()

    then:
    subject.getWrapped()
    result.is(subject)
  }

  def "setWrapped and getWrapped"() {
    when:
    subject.setWrapped(true)

    then:
    subject.getWrapped()

    when:
    subject.setWrapped(false)

    then:
    !subject.getWrapped()
  }
}
