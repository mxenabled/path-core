package com.mx.common.models

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
}
