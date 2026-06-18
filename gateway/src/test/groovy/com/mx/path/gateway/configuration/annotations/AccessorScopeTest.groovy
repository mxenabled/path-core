package com.mx.path.gateway.configuration.annotations

import spock.lang.Specification

class AccessorScopeTest extends Specification {

  def "enum constants have expected name and value"() {
    expect:
    AccessorScope.SINGLETON.getName() == "singleton"
    AccessorScope.SINGLETON.getValue() == 3
    AccessorScope.PROTOTYPE.getName() == "prototype"
    AccessorScope.PROTOTYPE.getValue() == 1
  }

  def "resolve(int) returns matching scope"() {
    expect:
    AccessorScope.resolve(3) == AccessorScope.SINGLETON
    AccessorScope.resolve(1) == AccessorScope.PROTOTYPE
  }

  def "resolve(int) returns null for unknown value"() {
    expect:
    AccessorScope.resolve(99) == null
  }

  def "resolve(String) returns matching scope"() {
    expect:
    AccessorScope.resolve("singleton") == AccessorScope.SINGLETON
    AccessorScope.resolve("prototype") == AccessorScope.PROTOTYPE
  }

  def "resolve(String) returns null for unknown name"() {
    expect:
    AccessorScope.resolve("unknown") == null
    AccessorScope.resolve(null) == null
  }
}
