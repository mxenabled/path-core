package com.mx.path.core.common.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import lombok.Getter
import lombok.Setter
import spock.lang.Specification

import java.util.regex.Pattern

class PatternTypeAdapterTest extends Specification {
  Gson subject

  class PatternSerializationTest {
    @Getter
    @Setter
    private Pattern pattern;
  }

  def setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Pattern, new PatternTypeAdapter())
        .create()
  }

  def "serializes and deserializes"() {
    given:
    def target = new PatternSerializationTest()
    target.pattern = Pattern.compile("[abcd]*")

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, PatternSerializationTest)

    then:
    deserialized.pattern.pattern() == "[abcd]*"

    when: "handles null"
    target.pattern = null

    serialized = subject.toJson(target)
    deserialized = subject.fromJson(serialized, PatternSerializationTest)

    then:
    deserialized.pattern == null
  }
}
