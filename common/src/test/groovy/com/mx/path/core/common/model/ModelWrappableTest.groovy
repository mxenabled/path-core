package com.mx.path.core.common.model


import com.mx.testing.TestAccount

import spock.lang.Specification

class ModelWrappableTest extends Specification {

  def "testWrappedIsChainableMethod"() {
    given:
    def subject = new ModelList<TestAccount>()

    expect: "Wrapped should be a chain method"
    subject == subject.wrapped()
    subject.getWrapped()
  }
}
