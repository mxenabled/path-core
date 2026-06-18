package com.mx.path.testing

import spock.lang.Specification

class WithMockeryTest extends Specification implements WithMockery {

  def "setupMockery and cleanupMockery complete without error"() {
    when:
    setupMockery()
    cleanupMockery()

    then:
    noExceptionThrown()
  }
}
