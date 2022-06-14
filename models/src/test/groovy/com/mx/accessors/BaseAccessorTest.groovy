package com.mx.accessors


import com.mx.testing.BaseAccessorTestImplemented

import spock.lang.Specification

class BaseAccessorTest extends Specification {
  AccessorConfiguration configuration

  def setup() {
    configuration = AccessorConfiguration.builder().build()
  }

  def "test getChildAccessors"() {
    given:
    def subject = new BaseAccessorTestImplemented(configuration)

    when:
    def result = subject.getChildAccessors()

    then: "It contains accessors for implemented AccountBaseTestAccessor AND StatusBaseAccessor"
    result.size() == 2
    result.any({it.accessorClass == BaseAccessorTestImplemented.AccountBaseTestAccessor})
    result.any({it.accessorClass == StatusDefaultAccessor })
    result[0].base.accessorClass == BaseAccessor
    result.every({ it.base.accessorClass == BaseAccessor })
  }

  def "test getAccessorBase"() {
    when:
    def subclass = Accessor.getAccessorBase(BaseAccessorTestImplemented)

    then:
    subclass == BaseAccessor

    when: "self is base class"
    subclass = Accessor.getAccessorBase(BaseAccessor)

    then:
    subclass == BaseAccessor
  }
}
