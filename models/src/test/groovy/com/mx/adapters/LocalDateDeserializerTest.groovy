package com.mx.adapters

import java.time.LocalDate

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException

import spock.lang.Specification
import spock.lang.Unroll

class LocalDateDeserializerTest extends Specification {

  class TestType {
    public LocalDate date;
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


  def "deserialize with error"() {
    given:
    def subject = LocalDateDeserializer.builder().format("MM/dd/yyyy").format("yyyy-MM-dd").build()
    def gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, subject).create()

    when:
    gson.fromJson("{ \"date\": \"2/1/2000\" }", TestType.class).date

    then:
    thrown(JsonParseException)
  }
}
