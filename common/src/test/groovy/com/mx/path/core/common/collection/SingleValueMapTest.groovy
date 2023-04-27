package com.mx.path.core.common.collection


import spock.lang.Specification

class SingleValueMapTest extends Specification {
  SingleValueMap subject

  def setup() {
    subject = new SingleValueMap<String, String>()
  }

  def "forMap"() {
    given:
    Map<String, List<String>> rawMap = new LinkedHashMap<>()

    when:
    subject = SingleValueMap.forMap(rawMap)

    then:
    subject.getRawMap() === rawMap
  }

  def "constructor"() {
    given:
    def multi = new MultiValueMap<String, String>()
    multi.add("key1", "value1")
    multi.add("key1", "value2")

    Map<String, String> singleMap = new LinkedHashMap<String, String>()
    singleMap.put("key1", "value1")

    when:
    subject = new SingleValueMap<String, String>(multi)

    then:
    subject.getRawMap() == multi.getRawMap()
    subject.toMultiValueMap().get("key1").get(0) == "value1"
    subject.toMultiValueMap().get("key1").get(1) == "value2"
  }

  def "size"() {
    when:
    subject.put("key1", "value1")
    subject.put("key2", "value2")
    subject.put("key3", "value3")

    then:
    subject.size() == 3

    when:
    def empty = new SingleValueMap<String, String>()

    then:
    empty.size() == 0
  }

  def "isEmpty"() {
    when:
    subject.clear()

    then:
    subject.isEmpty()

    when:
    subject.put("numbers", "3")

    then:
    !subject.isEmpty()

    when:
    def empty = new SingleValueMap<String, String>()

    then:
    empty.isEmpty()
  }

  def "containsKey"() {
    when:
    subject.put("numbers", "1")
    subject.put("letters", "a")

    then:
    subject.containsKey("numbers")
    subject.containsKey("letters")
  }

  def "containsValue"() {
    when:
    subject.put("key1", "value1")
    subject.put("key2", "value2")

    then:
    subject.containsValue("value1")
    subject.containsValue("value2")
  }

  def "get"() {
    when:
    subject.put("key1", "value1")

    then:
    subject.get("key1") == "value1"
    subject.get("nonKey") == null
    subject.get(null) == null
  }

  def "put"() {
    when:
    subject.put("key1", "value1")

    then:
    subject.get("key1") == "value1"
  }

  def "remove"() {
    given:
    subject.put("key1", "value1")

    when:
    def value = subject.remove("key1")

    then:
    value == "value1"
    subject.get("key1") == null
    subject.isEmpty()

    when:
    value = subject.remove("nonKey")

    then:
    value == null
  }

  def "putAll"() {
    when:
    def toPut = new HashMap().tap {
      put("numbers", new LinkedList().tap {
        add("1")
        add("2")
        add("3")
      })
      put("letters", new LinkedList().tap {
        add("a")
        add("b")
        add("c")
      })
    }
    subject.putAll(toPut)

    then:
    subject.get("numbers") == ["1", "2", "3"]
    subject.get("letters") == ["a", "b", 'c']
  }

  def "clear"() {
    given:
    subject.put("key1", "value1")

    when:
    subject.clear()

    then:
    subject.isEmpty()
  }

  def "keySet"() {
    given:
    subject.put("key1", "value1")

    expect:
    subject.keySet() == ["key1"].toSet()
  }

  def "values"() {
    given:
    subject.put("key1", "value1")

    expect:
    subject.values().toArray() == ["value1"].toArray()
  }

  def "entrySet"() {
    given:
    subject.put("key1", "value1")
    subject.put("key2", "value2")
    subject.put("key3", null)

    expect:
    subject.entrySet() == [key1: "value1", key2: "value2", key3: null].entrySet()
  }

  def "equals"() {
    given:
    subject.put("key1", "value1")
    def copy = new SingleValueMap()
    copy.put("key1", "value1")

    expect:
    subject.equals(copy)
    !subject.equals(new LinkedHashMap())
  }

  def "toMultiValueMap"() {
    given:
    subject.put("key1", "value1")

    when:
    def multimap = subject.toMultiValueMap()

    then:
    multimap.get("key1").get(0) == "value1"
  }

  def "multiple puts act as single-map"() {
    given:
    def multimap = subject.toMultiValueMap()

    when:
    subject.put("key1", "value1")
    subject.put("key1", "value2")

    then:
    multimap.get("key1").size() == 1
    multimap.get("key1").get(0) == "value2"
  }

  def "flatEntrySet renders key once per value"() {
    given:
    def multimap = subject.toMultiValueMap()

    when:
    multimap.add("key1", "value1")
    multimap.add("key1", "value2")
    multimap.add("key2", "value1")

    then:
    subject.flatEntrySet().size() == 3
    subject.flatEntrySet().asCollection()[0].key == "key1"
    subject.flatEntrySet().asCollection()[0].value == "value1"
    subject.flatEntrySet().asCollection()[1].key == "key1"
    subject.flatEntrySet().asCollection()[1].value == "value2"
    subject.flatEntrySet().asCollection()[2].key == "key2"
    subject.flatEntrySet().asCollection()[2].value == "value1"
  }
}
