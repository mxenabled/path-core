package com.mx.adapters

import java.time.LocalDate
import java.time.ZoneOffset

import com.google.gson.GsonBuilder

import spock.lang.Specification
import spock.lang.Unroll


class DateDeserializerTest extends Specification {

  class TestType {
    public Date date
  }

  @Unroll
  def "deserialize"() {
    given:
    def gson = new GsonBuilder().registerTypeAdapter(Date.class, DateDeserializer.builder().format(format).build()).create()

    when:
    def result = gson.fromJson("{ \"date\": \"$str\" }", TestType.class).date

    then:
    result.toString() == expected

    where:
    str                    || format                   || expected
    "1997-07-16T00:00"     || "yyyy-MM-dd'T'HH:mm"     || java.util.Date .from(LocalDate.of( 1997 , 7 , 16 ).atStartOfDay(ZoneOffset.UTC).toInstant()).toString()
    "1997-07-16T00:00:00"  || "yyyy-MM-dd'T'HH:mm:ss"  || java.util.Date .from(LocalDate.of( 1997 , 7 , 16 ).atStartOfDay(ZoneOffset.UTC).toInstant()).toString()
  }
}
