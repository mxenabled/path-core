package com.mx.path.testing

import com.mx.path.testing.Mockery

class MockerySpec extends Mockery {

  def "setupMockery and cleanupMockery complete without error"() {
    when:
    setupMockery()
    cleanupMockery()

    then:
    noExceptionThrown()
  }
}
