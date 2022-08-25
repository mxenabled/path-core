package com.mx.models

import com.mx.testing.Account

import spock.lang.Specification

class MdxBaseTest extends Specification {
  def subject

  def setup() {
    subject = new Account()
    subject.appendWarning(new Warning("Some warning"))
    subject.appendWarning(new Warning("Another warning"))
  }

  def "getObjectMetadata"() {
    when:
    def metadata = subject.getObjectMetadata()

    then:
    metadata != null
  }

  def "getWarnings"() {
    when:
    def warnings = subject.getWarnings()

    then:
    warnings.size() == 2
  }
}
