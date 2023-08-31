package com.mx.path.core.common.serialization

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.path.core.common.accessor.UnauthorizedException
import com.mx.testing.serialization.SystemSerializationType

import spock.lang.Specification

class SystemTypeAdapterFactoryTest extends Specification {
  Gson subject

  def setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(SystemTypeAdapterFactory.builder().build())
        .create()
  }

  def "handles exception with cause"() {
    given:
    def cause = new IllegalArgumentException("Password")
    def throwable = new UnauthorizedException("Unauthorized", "Wrong password", cause)

    SystemSerializationType target = SystemSerializationType.builder()
        .throwable(throwable)
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    verifyAll(deserialized) {
      throwable != null
      throwable instanceof UnauthorizedException
      throwable.cause != null
      throwable.cause instanceof IllegalArgumentException
    }
  }

  def "handles date times"() {
    given:
    def target = SystemSerializationType.builder()
        .localDate(LocalDate.of(2015, 10, 21))
        .localDateTime(LocalDateTime.of(2015, 10, 21, 4, 29))
        .zonedDateTime(ZonedDateTime.of(2015, 10, 21, 4, 29, 0, 0, ZoneId.of("-04:00")))
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    verifyAll(deserialized) {
      localDate == LocalDate.of(2015, 10, 21)
      localDateTime == LocalDateTime.of(2015, 10, 21, 4, 29)
      zonedDateTime == ZonedDateTime.of(2015, 10, 21, 4, 29, 0, 0, ZoneId.of("-04:00"))
    }
  }
}
