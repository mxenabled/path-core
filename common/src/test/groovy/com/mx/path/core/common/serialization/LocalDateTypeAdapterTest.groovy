package com.mx.path.core.common.serialization

import java.time.LocalDate

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.mx.testing.serialization.ClassWithLocalDate

import spock.lang.Specification
import spock.lang.Unroll

class LocalDateTypeAdapterTest extends Specification {

  @Unroll
  def "deserialize"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateTypeAdapter.builder().format(format).build()).create()

    when:
    def result = gson.fromJson("{ \"localDate\": \"$str\" }", ClassWithLocalDate.class).localDate

    then:
    result == expected

    where:
    str              || format         || expected
    "10/11/2000"     || "M/d/yyyy"     || LocalDate.of(2000, 10, 11)
    "1/1/2000"       || "M/d/yyyy"     || LocalDate.of(2000, 1, 1)
  }

  def "deserialize with multiple formats"() {
    given:
    def subject = LocalDateTypeAdapter.builder().format("M/d/yyyy").format("yyyy-MM-dd").build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when:
    def result = gson.fromJson("{ \"localDate\": \"2/1/2000\" }", ClassWithLocalDate.class).localDate

    then:
    result == LocalDate.of(2000, 2, 1)

    when:
    result = gson.fromJson("{ \"localDate\": \"2000-02-01\" }", ClassWithLocalDate.class).localDate

    then:
    result == LocalDate.of(2000, 2, 1)
  }

  def "deserialize with default format"() {
    given:
    def subject = LocalDateTypeAdapter.builder().build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when: "default string format"
    def result = gson.fromJson("{ \"localDate\": \"2000-02-01\" }", ClassWithLocalDate.class).localDate

    then:
    result == LocalDate.of(2000, 2, 1)

    when: "object format"
    result = gson.fromJson("{ \"localDate\": { \"year\": \"2000\", \"month\": \"02\", \"day\": \"01\" } }", ClassWithLocalDate.class).localDate

    then:
    result == LocalDate.of(2000, 2, 1)
  }

  def "deserialize with error"() {
    given:
    def subject = LocalDateTypeAdapter.builder().format("MM/dd/yyyy").format("yyyy-MM-dd").build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when:
    gson.fromJson("{ \"localDate\": \"2/1/2000\" }", ClassWithLocalDate.class).localDate

    then:
    thrown(JsonParseException)
  }

  def "serialize with default format"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateTypeAdapter.builder().build()).create()

    when:
    def date = LocalDate.of(2000, 10, 11)
    def result = gson.toJson(date)

    then:
    result == "{\"year\":2000,\"month\":10,\"day\":11}"
  }

  def "serialize with format"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateTypeAdapter.builder().serializeFormat(format).build()).create()

    when:
    def result = gson.toJson(new ClassWithLocalDate().tap { it.localDate = date })

    then:
    result == "{\"localDate\":\"${expected}\"}"

    where:
    format        || date                        || expected
    "MM-dd-yyyy"  || LocalDate.of(2000, 10, 11)  || "10-11-2000"
    "M-d-yyyy"    || LocalDate.of(2000, 1, 1)    || "1-1-2000"
  }

  def "LocalDateDeserializer compatibility"() {
    given:
    def localDateDeserializer = new GsonBuilder()
        .registerTypeAdapter(LocalDate, LocalDateDeserializer.builder().build())
        .create()

    def localDateTypeAdapter = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateTypeAdapter.builder().build()).create()

    def target = new ClassWithLocalDate().tap { it.localDate = LocalDate.of(2023, 6, 11) }

    when:
    def serialized = localDateTypeAdapter.toJson(target)
    def deserialized = localDateDeserializer.fromJson(serialized, ClassWithLocalDate)

    then:
    deserialized.localDate == LocalDate.of(2023, 6, 11)

    when:
    serialized = localDateDeserializer.toJson(target)
    deserialized = localDateTypeAdapter.fromJson(serialized, ClassWithLocalDate)

    then:
    deserialized.localDate == LocalDate.of(2023, 6, 11)
  }
}
