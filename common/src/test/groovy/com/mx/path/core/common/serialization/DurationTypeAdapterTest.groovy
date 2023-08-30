package com.mx.path.core.common.serialization

import java.time.Duration

import lombok.Getter
import lombok.Setter

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import spock.lang.Specification

class DurationTypeAdapterTest extends Specification {
  Gson subject

  class DurationSerializationTest {
    @Getter
    @Setter
    private Duration duration;
  }

  def setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Duration, new DurationTypeAdapter())
        .create()
  }

  def "serializes and deserializes"() {
    given:
    def target = new DurationSerializationTest()
    target.duration = Duration.ofSeconds(32)

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, DurationSerializationTest)

    then:
    deserialized.duration.getSeconds() == 32

    when: "handles null"
    target.duration = null

    serialized = subject.toJson(target)
    deserialized = subject.fromJson(serialized, DurationSerializationTest)

    then:
    deserialized.duration == null
  }
}
