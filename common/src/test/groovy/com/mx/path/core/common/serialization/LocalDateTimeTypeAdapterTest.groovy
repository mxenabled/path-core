package com.mx.path.core.common.serialization

import java.time.LocalDate
import java.time.LocalDateTime

import com.google.gson.GsonBuilder
import com.mx.testing.serialization.ClassWithLocalDateTime

import spock.lang.Specification

class LocalDateTimeTypeAdapterTest extends Specification {

  def "deserialize string dates"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder()
        .format("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        .build())
        .create()

    String json = "{\n" +
        "  \"localDateTime\": \"2022-12-06T18:34:22.990000\"\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithLocalDateTime)

    then:
    verifyAll (result.localDateTime) {
      getYear() == 2022
      getMonthValue() == 12
      getDayOfMonth() == 6
      getHour() == 18
      getMinute() == 34
      getSecond() == 22
      getNano() == 990000000
    }
  }

  def "deserialize string dates (default)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder()
        .build())
        .create()

    String json = "{\n" +
        "  \"localDateTime\": \"2022-12-06T18:34:22.990\"\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithLocalDateTime)

    then:
    verifyAll (result.localDateTime) {
      getYear() == 2022
      getMonthValue() == 12
      getDayOfMonth() == 6
      getHour() == 18
      getMinute() == 34
      getSecond() == 22
      getNano() == 990000000
    }
  }

  def "deserialize handles null"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder()
        .build())
        .create()

    String json = "{\n" +
        "  \"localDateTime\": null\n" +
        "}"

    when:
    def result = subject.fromJson(json, ClassWithLocalDateTime)

    then:
    result.localDateTime == null
  }

  def "deserialize from object (Java 8)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder().build())
        .create()

    String json = "{\n" +
        "  \"localDateTime\": {\n" +
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
    def result = subject.fromJson(json, ClassWithLocalDateTime)

    then:
    verifyAll (result.localDateTime) {
      getYear() == 2022
      getMonthValue() == 8
      getDayOfMonth() == 12
      getHour() == 18
      getMinute() == 20
      getSecond() == 48
      getNano() == 878000000
    }
  }

  def "Serialize as string"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder()
        .serializeFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        .build())
        .create()

    def obj = new ClassWithLocalDateTime().tap {
      setLocalDateTime(LocalDateTime.of(2022, 8, 12, 14, 34, 42, 940000000))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"localDateTime\":\"2022-08-12T14:34:42.940\"}"
  }

  def "Serialize as object (default)"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder().build())
        .create()

    def obj = new ClassWithLocalDateTime().tap {
      setLocalDateTime(LocalDateTime.of(2022, 8, 12, 14, 34, 42, 940009))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"localDateTime\":{\"time\":{\"hour\":14,\"minute\":34,\"second\":42,\"nano\":940009},\"date\":{\"year\":2022,\"month\":8,\"day\":12}}}"
  }

  def "Serialize as object"() {
    given:
    def subject = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeTypeAdapter.builder().serializeFormat("OBJECT")
        .build())
        .create()

    def obj = new ClassWithLocalDateTime().tap {
      setLocalDateTime(LocalDateTime.of(2022, 8, 12, 14, 34, 42, 940009))
    }

    when:
    def result = subject.toJson(obj)

    then:
    result == "{\"localDateTime\":{\"time\":{\"hour\":14,\"minute\":34,\"second\":42,\"nano\":940009},\"date\":{\"year\":2022,\"month\":8,\"day\":12}}}"
  }

  def "LocalDateDeserializer compatibility"() {
    given:
    def localDateDeserializer = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime, LocalDateTimeDeserializer.builder().build())
        .create()

    def localDateTypeAdapter = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, LocalDateTimeTypeAdapter.builder().build()).create()

    def target = new ClassWithLocalDateTime().tap { it.localDateTime = LocalDateTime.of(2015, 10, 21, 4, 29, 0) }

    when:
    def serialized = localDateTypeAdapter.toJson(target)
    def deserialized = localDateDeserializer.fromJson(serialized, ClassWithLocalDateTime)

    then:
    deserialized.localDateTime == LocalDateTime.of(2015, 10, 21, 4, 29, 0)

    when:
    serialized = localDateDeserializer.toJson(target)
    deserialized = localDateTypeAdapter.fromJson(serialized, ClassWithLocalDateTime)

    then:
    deserialized.localDateTime == LocalDateTime.of(2015, 10, 21, 4, 29, 0)
  }
}
