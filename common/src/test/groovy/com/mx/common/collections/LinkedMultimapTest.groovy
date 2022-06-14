package com.mx.common.collections

import spock.lang.Specification

class LinkedMultimapTest extends Specification {
  LinkedMultimap subject

  def setup() {
    subject = new LinkedMultimap<String, String>()
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
    def empty = new LinkedMultimap<String, String>()

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
    def empty = new LinkedMultimap<String, String>()

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
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    then:
    subject.containsValue(new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    !subject.containsValue(new LinkedList().tap {
      add("1")
      add("2")
      add("3")
      add("4")
    })
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

    expect:
    subject.entrySet() == [numbers: ["1", "2", "3"], letters: ["a", "b", "c"]].entrySet()
  }

  def "equals"() {
    given:
    subject.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })
    def copy = new LinkedMultimap()
    copy.addAll("numbers", new LinkedList().tap {
      add("1")
      add("2")
      add("3")
    })

    expect:
    subject.equals(copy)
    !subject.equals(new LinkedHashMap())
  }
}
