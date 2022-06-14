package com.mx.common.collections

import spock.lang.Specification

class MultiValueMapTest extends Specification {
  MultiValueMap subject

  def setup() {
    subject = new MultiValueMap<String, String>()
  }

  def "forMap"() {
    given:
    Map<String, List<String>> rawMap = new LinkedHashMap<>()

    when:
    subject = MultiValueMap.forMap(rawMap)

    then:
    subject.getRawMap() === rawMap
  }

  def "getFirst"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    subject.addAll("empty", new LinkedList())

    when: "There is a collection of values"
    def first = subject.getFirst("numbers")

    then:
    first == "1"

    when: "Values is empty"
    first = subject.getFirst("empty")

    then:
    first == null
  }

  def "add"() {
    given:
    subject.add("key", "value")

    expect:
    subject.get("key") == ["value"]
  }

  def "addIfAbsent"() {
    given:
    subject.addIfAbsent("newKey", "newValue1")
    subject.addIfAbsent("newKey", "newValue2")

    expect:
    subject.get("newKey").size() == 1
    subject.get("newKey").get(0) == "newValue1"
  }

  def "addAll"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    expect:
    subject.get("numbers") == ["1", "2", "3"]
  }

  def "set"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    when:
    subject.set("numbers", "4")

    then:
    subject.get("numbers") == ["4"]
  }

  def "toSingleValueMap"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    subject.addAll("letters", new LinkedList().tap {
      add("a")
      add("b")
      add("c")
    })
    subject.addAll("words", new LinkedList().tap {
      add("one")
      add("two")
      add("three")
    })

    when:
    def singleValueMap = subject.toSingleValueMap()

    then:
    singleValueMap == [numbers: "1", letters: "a", words: "one"]
  }

  def "size"() {
    when:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.size() == 1

    when:
    def empty = new MultiValueMap<String, String>()

    then:
    empty.size() == 0
  }

  def "isEmpty"() {
    when:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    !subject.isEmpty()

    when:
    def empty = new MultiValueMap<String, String>()

    then:
    empty.isEmpty()
  }

  def "containsKey"() {
    when:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.containsKey("numbers")
    !subject.containsKey("letters")
  }

  def "containsValue"() {
    when:
    subject.addAll("key1", new LinkedList().tap {
      add("value1")
      add("value2")
    })

    subject.addAll("key2", new LinkedList().tap {
      add("value3")
      add("value4")
    })

    then:
    subject.containsValue(new LinkedList().tap {
      add("value1")
      add("value2")
    })

    !subject.containsValue(new LinkedList().tap { add("junk") })

    subject.containsValue("value2")
  }

  def "flatValues"() {
    when:
    subject.addAll("key1", new LinkedList().tap {
      add("value1")
      add("value2")
      add("value3")
    })
    subject.addAll("key2", new LinkedList().tap { add("value4") })

    then:
    subject.flatValues() == [
      "value1",
      "value2",
      "value3",
      "value4"
    ]
  }

  def "get"() {
    when:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.get("numbers") == ["1", "2", "3"]
  }

  def "put"() {
    when:
    subject.put("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.get("numbers") == ["1", "2", "3"]
  }

  def "remove"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    when:
    subject.remove("numbers")

    then:
    subject.get("numbers") == null
    subject.isEmpty()
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
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    when:
    subject.clear()

    then:
    subject.isEmpty()
  }

  def "keySet"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    expect:
    subject.keySet() == ["numbers"].toSet()
  }

  def "values"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    subject.addAll("letters", new LinkedList().tap {
      add("a")
      add("b")
      add("c")
    })

    expect:
    subject.values().toArray() == [
      ["1", "2", "3"],
      ["a", "b", "c"]
    ].toArray()
  }

  def "entrySet"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    subject.addAll("letters", new LinkedList().tap {
      add("a")
      add("b")
      add("c")
    })
    subject.addAll("nullValue", new LinkedList().tap { add(null) })

    expect:
    subject.entrySet() == [numbers: ["1", "2", "3"], letters: ["a", "b", "c"], nullValue: [null]].entrySet()
  }

  def "equals"() {
    when:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    def copy = new MultiValueMap<String, String>()
    copy.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.equals(copy)
    !subject.equals(new LinkedHashMap())

    when:
    subject = new MultiValueMap<String, String>()
    subject.add("key1", "value1")
    def other = new MultiValueMap<String, String>(subject)

    then:
    subject.equals(other)

    when:
    subject = new MultiValueMap<String, String>()
    subject.add("key1", "value1")
    other = new MultiValueMap<String, String>()
    other.add("key1", "value1")

    then:
    subject.equals(other)

    when:
    subject = new MultiValueMap<String, String>()
    subject.add("key1", "value1")
    other = new MultiValueMap<String, String>()
    other.add("key1", "value2")

    then:
    !subject.equals(other)
  }

  def "flatMap renders key once per value"() {
    when:
    subject.add("key1", "value1")
    subject.add("key1", "value2")
    subject.add("key2", "value1")

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
