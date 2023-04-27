package com.mx.path.core.common.reflection


import com.mx.testing.ConstructorsTestClass

import spock.lang.Specification

class ConstructorsTest extends Specification {

  def "getNoArgumentConstructor"() {
    when:
    def constructor = Constructors.getNoArgumentConstructor(ConstructorsTestClass.class)

    then:
    constructor != null
  }

  def "instantiateWithNoArgumentConstructor"() {
    when:
    def instance = Constructors.instantiateWithNoArgumentConstructor(ConstructorsTestClass)

    then:
    instance.getId() == 0
  }
}
