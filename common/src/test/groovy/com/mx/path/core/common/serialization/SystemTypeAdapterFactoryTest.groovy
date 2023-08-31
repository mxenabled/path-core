package com.mx.path.core.common.serialization

import java.time.LocalDate
import java.time.LocalDateTime

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
    deserialized.throwable != null
    deserialized.throwable instanceof UnauthorizedException
    deserialized.throwable.cause != null
    deserialized.throwable.cause instanceof IllegalArgumentException
  }

  def "handles date times"() {
    given:
    def target = SystemSerializationType.builder()
        .localDate(LocalDate.of(2015, 10, 21))
        .localDateTime(LocalDateTime.of(2015, 10, 21, 4, 29))
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    deserialized.localDate == LocalDate.of(2015, 10, 21)
    deserialized.localDateTime == LocalDateTime.of(2015, 10, 21, 4, 29)
  }
}
