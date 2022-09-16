package com.mx.common.models

import com.mx.testing.Account

import spock.lang.Specification

class MdxWrappableTest extends Specification {

  def "testWrappedIsChainableMethod"() {
    given:
    def subject = new MdxList<Account>()

    expect: "Wrapped should be a chain method"
    subject == subject.wrapped()
    subject.getWrapped()
  }
}
