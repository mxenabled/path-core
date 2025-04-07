package com.mx.path.core.common.serialization

import java.time.LocalDate

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException

import spock.lang.Specification
import spock.lang.Unroll

class LocalDateDeserializerTest extends Specification {

  class TestType {
    public LocalDate date
  }

  @Unroll
  def "deserialize"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().format(format).build()).create()

    when:
    def result = gson.fromJson("{ \"date\": \"$str\" }", TestType.class).date

    then:
    result == expected

    where:
    str              || format         || expected
    "10/11/2000"     || "M/d/yyyy"     || LocalDate.of(2000, 10, 11)
    "1/1/2000"       || "M/d/yyyy"     || LocalDate.of(2000, 1, 1)
  }

  def "deserialize with multiple formats"() {
    given:
    def subject = LocalDateDeserializer.builder().format("M/d/yyyy").format("yyyy-MM-dd").build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when:
    def result = gson.fromJson("{ \"date\": \"2/1/2000\" }", TestType.class).date

    then:
    result == LocalDate.of(2000, 2, 1)

    when:
    result = gson.fromJson("{ \"date\": \"2000-02-01\" }", TestType.class).date

    then:
    result == LocalDate.of(2000, 2, 1)
  }

  def "deserialize with default format"() {
    given:
    def subject = LocalDateDeserializer.builder().build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when: "default string format"
    def result = gson.fromJson("{ \"date\": \"2000-02-01\" }", TestType.class).date

    then:
    result == LocalDate.of(2000, 2, 1)

    when: "object format"
    result = gson.fromJson("{ \"date\": { \"year\": \"2000\", \"month\": \"02\", \"day\": \"01\" } }", TestType.class).date

    then:
    result == LocalDate.of(2000, 2, 1)
  }

  def "deserialize with error"() {
    given:
    def subject = LocalDateDeserializer.builder().format("MM/dd/yyyy").format("yyyy-MM-dd").build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when:
    gson.fromJson("{ \"date\": \"2/1/2000\" }", TestType.class).date

    then:
    thrown(JsonParseException)
  }

  def "serialize with default format"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().build()).create()

    when:
    def date = LocalDate.of(2000, 10, 11)
    def result = gson.toJson(date)

    then:
    result == "{\"day\":11,\"month\":10,\"year\":2000}"
  }

  def "serialize with format"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().serializeFormat(format).build()).create()

    when:
    def result = gson.toJson(new TestType().tap { it.date = date })

    then:
    result == "{\"date\":\"${expected}\"}"

    where:
    format        || date                        || expected
    "MM-dd-yyyy"  || LocalDate.of(2000, 10, 11)  || "10-11-2000"
    "M-d-yyyy"    || LocalDate.of(2000, 1, 1)    || "1-1-2000"
  }
}
