package com.mx.common.collections

import spock.lang.Specification
import spock.lang.Unroll

class ObjectArrayTest extends Specification {
  ObjectArray subject

  void setup() {
    subject = new ObjectArray()
  }

  def "createArray"() {
    when:
    def array = subject.createArray()

    then:
    array.getClass() == ObjectArray.class
    subject.size() == 1
  }

  def "createMap"() {
    when:
    def map = subject.createMap()

    then:
    map.getClass() == ObjectMap.class
    subject.size() == 1
  }

  def "getArray"() {
    given:
    subject.createArray().add("some_value")
    subject.createMap()
    subject.add("not an array")
    subject.add(null)

    when:
    def array = subject.getArray(0)

    then:
    array.getClass() == ObjectArray.class
    array.size() == 1

    when: "Get the map"
    array = subject.getArray(1)

    then: "should return null"
    array == null

    when: "Get the value"
    array = subject.getArray(2)

    then: "should return null"
    array == null

    when: "Get null"
    array = subject.getArray(3)

    then: "should return null"
    array == null

    when: "Get off end of array"
    array = subject.getArray(4)

    then: "should return null"
    array == null
  }

  def "getMap"() {
    given:
    subject.createMap().put("key", "value")
    subject.createArray().add("some_value")
    subject.add("not an array")
    subject.add(null)

    when:
    def map = subject.getMap(0)

    then:
    map.getClass() == ObjectMap.class
    map.size() == 1
    map.get("key") == "value"

    when: "Get the array"
    map = subject.getMap(1)

    then: "should return null"
    map == null

    when: "Get the value"
    map = subject.getMap(2)

    then: "should return null"
    map == null

    when: "Get null"
    map = subject.getMap(3)

    then: "should return null"
    map == null

    when: "Get off end of array"
    map = subject.getMap(4)

    then: "should return null"
    map == null
  }

  def "getAsDefault"() {
    given:
    subject.add("value")

    when: "get the value"
    def value = subject.getAs(String.class, 0, "null")

    then: "returns the value"
    value == "value"

    when: "get off end of array"
    value = subject.getAs(String.class, 1, "null")

    then: "returns default"
    value == "null"

    when: "get with mismatching type"
    value = subject.getAs(ObjectMap.class, 1, new ObjectMap())

    then: "returns default"
    value.getClass() == ObjectMap.class
  }

  def "getAs"() {
    given:
    subject.add("value")
    subject.add(1)
    subject.createMap()

    when: "get the value"
    def value = subject.getAs(String.class, 0)

    then: "returns the value"
    value == "value"

    when: "get off end of array"
    value = subject.getAs(Integer.class, 1)

    then: "returns default"
    value == 1

    when: "get with mismatching type"
    value = subject.getAs(ObjectMap.class, 2)

    then: "returns default"
    value.getClass() == ObjectMap.class

    when: "get off end of array"
    value = subject.getAs(ObjectMap.class, 3)

    then: "returns default"
    value == null
  }

  def "isValue"() {
    when:
    subject.add("not an array")
    subject.add(1)
    subject.add(1.5)
    subject.createMap().put("key", "value")
    subject.createArray().add("some_value")
    subject.add(null)

    then: "string is value"
    subject.isValue(0)

    and: "integer is value"
    subject.isValue(1)

    and: "big decimal is value"
    subject.isValue(2)

    and: "map is not value"
    !subject.isValue(3)

    and: "array is not value"
    !subject.isValue(4)

    and: "null is not value"
    !subject.isValue(5)

    and: "off end of array is not value"
    !subject.isValue(6)
  }

  @Unroll("getAsBoolean #name")
  def "getAsBoolean"() {
    when:
    subject.add(value)

    then:
    subject.getAsBoolean(0) == result

    where:
    name                   || value          || result
    "one String"           || "1"            || true
    "zero String"          || "0"            || false
    "one Int"              || 1              || true
    "zero Int"             || 0              || false
    "one Double"           || 1.0            || true
    "String true"          || "true"         || true
    "Boolean true"         || true           || true
    "Boolean false"        || false          || false
  }

  @Unroll("getAsBoolean with default #name")
  def "getAsBoolean with default"() {
    when:
    if (value != null) {
      subject.add(value)
    }

    then:
    subject.getAsBoolean(0, defaultValue) == result

    where:
    name                   || value          || defaultValue     || result
    "one String"           || "1"            || false            || true
    "zero String"          || "0"            || true             || false
    "one Int"              || 1              || false            || true
    "zero Int"             || 0              || true             || false
    "one Double"           || 1.0            || false            || true
    "String true"          || "true"         || false            || true
    "Boolean true"         || true           || false            || true
    "Nothing true"         || null           || true             || true
    "Nothing false"        || null           || false            || false
  }

  def "getAsString"() {
    when:
    subject.add("1")
    subject.add(1)
    subject.add(1.5)
    subject.createMap()
    subject.createArray()

    then:
    subject.getAsString(0) == "1"
    subject.getAsString(1) == "1"
    subject.getAsString(2) == "1.5"
    subject.getAsString(3) == null
    subject.getAsString(4) == null

    subject.getAsString(3, "default") == "default"
    subject.getAsString(4, "default") == "default"
  }

  def "getAsInteger"() {
    when:
    subject.add("1")
    subject.add(1)
    subject.add(1.5)
    subject.add(10L)
    subject.createMap()
    subject.createArray()

    then:
    subject.getAsInteger(0) == 1
    subject.getAsInteger(1) == 1
    subject.getAsInteger(2) == 1
    subject.getAsInteger(3) == 10
    subject.getAsInteger(4) == null
    subject.getAsInteger(5) == null

    subject.getAsInteger(4, 20) == 20
    subject.getAsInteger(5, 20) == 20
  }

  def "getAsLong"() {
    when:
    subject.add("1")
    subject.add(1)
    subject.add(1.5)
    subject.add(10L)
    subject.createMap()
    subject.createArray()

    then:
    subject.getAsLong(0) == 1
    subject.getAsLong(1) == 1
    subject.getAsLong(2) == 1
    subject.getAsLong(3) == 10L
    subject.getAsLong(4) == null
    subject.getAsLong(5) == null

    subject.getAsLong(4, 20) == 20
    subject.getAsLong(5, 20) == 20
  }
}
