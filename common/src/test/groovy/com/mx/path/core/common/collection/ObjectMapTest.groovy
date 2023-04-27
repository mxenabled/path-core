package com.mx.path.core.common.collection


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ObjectMapTest extends Specification {
  @Shared
  ObjectMap subject

  void setup() {
    subject = new ObjectMap()
  }

  def "isValue"() {
    when:
    subject.put("value1", "1")
    subject.put("value2", 1)
    subject.put("value3", 1.0f)
    subject.put("value4", 1.0d)
    subject.createMap("obj1")
    subject.put("nothing", null)

    then:
    subject.isValue("value1")
    subject.isValue("value2")
    subject.isValue("value3")
    subject.isValue("value4")
    !subject.isValue("obj1")
    !subject.isValue("nothing")
    !subject.isValue("notThere")
  }

  def "isObj"() {
    when:
    subject.put("value1", "1")
    subject.createMap("obj1")
    subject.createArray("arr1")
    subject.put("nothing", null)

    then:
    subject.isMap("obj1")
    !subject.isMap("value1")
    !subject.isMap("arr1")
    !subject.isMap("nothing")
    !subject.isMap("notThere")
  }

  def "getObj"() {
    when:
    subject.put("value1", "1")
    subject.createMap("obj1")
    subject.createArray("arr1")

    then:
    subject.getMap("value1") == null
    subject.getMap("arr1") == null
    subject.getMap("obj1").getClass() == ObjectMap.class
  }

  def "isArray"() {
    when:
    subject.put("value1", "1")
    subject.createMap("obj1")
    subject.createArray("arr1")
    subject.put("nothing", null)

    then:
    !subject.isArray("obj1")
    !subject.isArray("value1")
    subject.isArray("arr1")
    !subject.isArray("nothing")
    !subject.isArray("notThere")
  }

  def "getArray"() {
    when:
    subject.put("value1", "1")
    subject.createMap("obj1")
    subject.createArray("arr1")

    then:
    subject.getArray("value1") == null
    subject.getArray("arr1").getClass() == ObjectArray.class
    subject.getArray("obj1") == null
  }

  def "getAs"() {
    when:
    subject.put("array", new ArrayList<>())

    then:
    subject.getAs(ArrayList.class, "array").class == ArrayList.class
  }

  def "getAs with default"() {
    when:
    subject.put("array", new ArrayList<>())

    then:
    subject.getAs(ArrayList.class, "array", new ArrayList<>()).class == ArrayList.class
    subject.getAs(ArrayList.class, "notThere", new ArrayList<>()).class == ArrayList.class
  }

  @Unroll("getAsBoolean #name")
  def "getAsBoolean"() {
    when:
    subject.put("value", value)

    then:
    subject.getAsBoolean("value") == result

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
      subject.put("value", value)
    }

    then:
    subject.getAsBoolean("value", defaultValue) == result

    where:
    name                   || value          || defaultValue || result
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
    subject.put("value1", "1")
    subject.put("int", 1)
    subject.put("decimal", new BigDecimal(1.5))
    subject.put("float", 1.5f)
    subject.put("double", 1.5d)
    subject.createMap("obj1")
    subject.createArray("arr1")

    then:
    subject.getAsString("value1") == "1"
    subject.getAsString("int") == "1"
    subject.getAsString("decimal") == "1.5"
    subject.getAsString("float") == "1.5"
    subject.getAsString("double") == "1.5"
    subject.getAsString("arr1") == null
    subject.getAsString("obj1") == null

    subject.getAsString("arr1", "default") == "default"
    subject.getAsString("obj1", "default") == "default"
  }

  def "getAsInteger"() {
    when:
    subject.put("value1", "1")
    subject.put("int", 1)
    subject.put("decimal", 1.5)
    subject.put("long", 10L)
    subject.createMap("obj1")
    subject.createArray("arr1")

    then:
    subject.getAsInteger("value1") == 1
    subject.getAsInteger("int") == 1
    subject.getAsInteger("decimal") == 1
    subject.getAsInteger("long") == 10
    subject.getAsInteger("arr1") == null
    subject.getAsInteger("obj1") == null

    subject.getAsInteger("arr1", 20) == 20
    subject.getAsInteger("obj1", 20) == 20
  }

  def "getAsLong"() {
    when:
    subject.put("value1", "1")
    subject.put("int", 1)
    subject.put("decimal", 1.5)
    subject.put("long", 10L)
    subject.createMap("obj1")
    subject.createArray("arr1")

    then:
    subject.getAsLong("value1") == 1L
    subject.getAsLong("int") == 1L
    subject.getAsLong("decimal") == 1L
    subject.getAsLong("long") == 10L
    subject.getAsLong("arr1") == null
    subject.getAsLong("obj1") == null

    subject.getAsLong("arr1", 20) == 20L
    subject.getAsLong("obj1", 20) == 20L
  }

  def "deepMerge"() {
    given:
    def a = new ObjectMap()
    def b = new ObjectMap()

    a.put("root1", 0)
    a.put("root2", 0)
    a.createMap("obj1")
    a.getMap("obj1").put("value1", 0)
    a.getMap("obj1").put("value3", 3)
    a.createArray("arr1")
    a.getArray("arr1").add(1)

    b.put("root1", 2)
    b.createMap("obj1")
    b.getMap("obj1").put("value1", 1)
    b.getMap("obj1").put("value2", 1)
    b.createArray("arr1")
    b.getArray("arr1").add(2)

    when:
    a.deepMerge(b)

    then:
    a.getAsInteger("root1") == 2
    a.getAsInteger("root2") == 0
    a.getMap("obj1").getAsInteger("value1") == 1
    a.getMap("obj1").getAsInteger("value3") == 3
    a.getArray("arr1").size() == 2
  }

  def "remove"() {
    given:
    def a = new ObjectMap()

    a.put("value1", 1)

    when:
    a.remove("value1")

    then:
    a.isEmpty()
  }
}
