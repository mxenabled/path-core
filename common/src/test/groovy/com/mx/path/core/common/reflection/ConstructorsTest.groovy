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

  def "safeGetConstructor"() {
    when:
    def constructor = Constructors.safeGetConstructor(ConstructorsTestClass.class, int.class)

    then:
    constructor != null

    when:
    constructor = Constructors.safeGetConstructor(ConstructorsTestClass.class, int.class, String.class)

    then:
    constructor == null
  }

  def "safeInstantiate"() {
    when:
    def constructor = Constructors.safeGetConstructor(ConstructorsTestClass.class, int.class)
    def instance = Constructors.safeInstantiate(constructor, 1)

    then:
    instance.id == 1

    when:
    instance = Constructors.safeInstantiate(null, 1)

    then:
    instance == null
  }
}
