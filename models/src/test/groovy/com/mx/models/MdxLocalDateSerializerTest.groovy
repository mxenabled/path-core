package com.mx.models

import java.time.LocalDate

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.mx.models.MdxLocalDateSerializer

import spock.lang.Specification

class MdxLocalDateSerializerTest extends Specification {
  Gson gson

  def setup() {
    gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(LocalDate.class, new MdxLocalDateSerializer())
        .setDateFormat("YYYY-MM-dd")
        .create()
  }

  def "serializingLocalDateTest"() {
    given:
    def date = LocalDate.of(2019, 1, 13)
    def resultStr = gson.toJson(date)

    expect:
    "\"2019-01-13\"" == resultStr
  }

  def "deserializingLocalDateHappyCase"() {
    given:
    def expectedDate = LocalDate.of(2019, 1, 13)
    def resultDate = gson.fromJson("\"2019-01-13\"", LocalDate.class)

    expect:
    expectedDate == resultDate
  }

  def "deserializingLocalDateWithBadInput"() {
    when:
    gson.fromJson("\"xxxxx\"", LocalDate.class)

    then:
    thrown(JsonParseException.class)
  }
}
