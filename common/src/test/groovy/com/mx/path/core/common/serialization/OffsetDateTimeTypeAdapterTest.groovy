package com.mx.path.core.common.serialization

import java.time.OffsetDateTime
import java.time.ZoneOffset

import com.google.gson.GsonBuilder
import com.mx.testing.serialization.ClassWithOffsetDateTime

import spock.lang.Specification

class OffsetDateTimeTypeAdapterTest extends Specification {

  def "deserialize string dates"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder()
        .format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        .build())
        .create()

    String json = "{\n" +
        "  \"offsetDateTime\": \"2022-12-06T18:34:22.990-06:00\"\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithOffsetDateTime)

    then:
    verifyAll (result.offsetDateTime) {
      getYear() == 2022
      getMonthValue() == 12
      getDayOfMonth() == 6
      getHour() == 18
      getMinute() == 34
      getSecond() == 22
      getNano() == 990000000
      getOffset() == ZoneOffset.ofHours(-6)
    }
  }

  def "deserialize string dates (default)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder()
        .build())
        .create()

    String json = "{\n" +
        "  \"offsetDateTime\": \"2022-12-06T18:34:22.990-06:00\"\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithOffsetDateTime)

    then:
    verifyAll (result.offsetDateTime) {
      getYear() == 2022
      getMonthValue() == 12
      getDayOfMonth() == 6
      getHour() == 18
      getMinute() == 34
      getSecond() == 22
      getNano() == 990000000
      getOffset() == ZoneOffset.ofHours(-6)
    }
  }

  def "deserialize handles null"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder()
        .build())
        .create()

    String json = "{\n" +
        "  \"offsetDateTime\": null\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithOffsetDateTime)

    then:
    result.offsetDateTime == null
  }

  def "deserialize from object (Java 8)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder().build())
        .create()

    String json = "{\n" +
        "  \"offsetDateTime\": {\n" +
        "    \"date\": {\n" +
        "      \"year\": 2022,\n" +
        "      \"month\": 8,\n" +
        "      \"day\": 12\n" +
        "    },\n" +
        "    \"time\": {\n" +
        "      \"hour\": 18,\n" +
        "      \"minute\": 20,\n" +
        "      \"second\": 48,\n" +
        "      \"nano\": 878000000,\n" +
        "      \"offset\": \"-06:00\"\n" +
        "    }\n" +
        "  }\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithOffsetDateTime)

    then:
    verifyAll (result.offsetDateTime) {
      getYear() == 2022
      getMonthValue() == 8
      getDayOfMonth() == 12
      getHour() == 18
      getMinute() == 20
      getSecond() == 48
      getNano() == 878000000
      getOffset() == ZoneOffset.ofHours(-6)
    }
  }

  def "deserialize from object (Java 8) missing offset"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder().defaultZoneOffset(ZoneOffset.ofHours(-4)).build())
        .create()

    String json = "{\n" +
        "  \"offsetDateTime\": {\n" +
        "    \"date\": {\n" +
        "      \"year\": 2022,\n" +
        "      \"month\": 8,\n" +
        "      \"day\": 12\n" +
        "    },\n" +
        "    \"time\": {\n" +
        "      \"hour\": 18,\n" +
        "      \"minute\": 20,\n" +
        "      \"second\": 48,\n" +
        "      \"nano\": 878000000\n" +
        "    }\n" +
        "  }\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithOffsetDateTime)

    then:
    verifyAll (result.offsetDateTime) {
      getYear() == 2022
      getMonthValue() == 8
      getDayOfMonth() == 12
      getHour() == 18
      getMinute() == 20
      getSecond() == 48
      getNano() == 878000000
      getOffset() == ZoneOffset.ofHours(-4)
    }
  }

  def "Serialize as string"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder()
        .serializeFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        .build())
        .create()

    def obj = new ClassWithOffsetDateTime().tap {
      setOffsetDateTime(OffsetDateTime.of(2022, 8, 12, 14, 34, 42, 940000000, ZoneOffset.ofHours(-6)))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"offsetDateTime\":\"2022-08-12T14:34:42.940-06:00\"}"
  }

  def "Serialize as object (default)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder().build())
        .create()

    def obj = new ClassWithOffsetDateTime().tap {
      setOffsetDateTime(OffsetDateTime.of(2022, 8, 12, 14, 34, 42, 940009, ZoneOffset.ofHours(-6)))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"offsetDateTime\":{\"time\":{\"hour\":14,\"minute\":34,\"second\":42,\"nano\":940009,\"offset\":\"-06:00\"},\"date\":{\"year\":2022,\"month\":8,\"day\":12}}}"
  }

  def "Serialize as object"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime, OffsetDateTimeTypeAdapter.builder().serializeFormat("OBJECT")
        .build())
        .create()

    def obj = new ClassWithOffsetDateTime().tap {
      setOffsetDateTime(OffsetDateTime.of(2022, 8, 12, 14, 34, 42, 940009, ZoneOffset.ofHours(-6)))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"offsetDateTime\":{\"time\":{\"hour\":14,\"minute\":34,\"second\":42,\"nano\":940009,\"offset\":\"-06:00\"},\"date\":{\"year\":2022,\"month\":8,\"day\":12}}}"
  }
}
