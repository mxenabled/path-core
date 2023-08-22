package com.mx.path.core.common.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.accessor.UnauthorizedException
import com.mx.path.core.common.exception.PathRequestException
import com.mx.path.core.common.exception.PathSystemException
import com.mx.testing.serialization.SystemSerializationType

import spock.lang.Specification

class SystemTypeAdapterTest extends Specification {
  Gson subject

  def setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(new SystemTypeAdapter())
        .create()
  }

  def "common path exception"() {
    given:
    def unauthorizedException
    try {
      try {
        throw new RuntimeException("WAT?!")
      } catch (RuntimeException e) {
        throw new UnauthorizedException("User dun messed up", "Git out'a here!", e)
        .withStatus(PathResponseStatus.ACCEPTED)
        .withCode("411")
        .withErrorTitle("Behold!")
        .withReason("Because")
        .withReport(true)
      }
    } catch (UnauthorizedException ex) {
      unauthorizedException = ex
    }

    SystemSerializationType target = SystemSerializationType.builder()
        .throwable(unauthorizedException)
        .build()

    when:
    def serialized = subject.toJson(target)
    println(serialized)

    SystemSerializationType deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    serialized != null
    deserialized != null
    verifyAll(deserialized) {
      getThrowable() instanceof UnauthorizedException
      verifyAll((UnauthorizedException) getThrowable()) {
        getCode() == "411"
        getStatus() == PathResponseStatus.ACCEPTED
        getMessage() == "User dun messed up"
        getUserMessage() == "Git out'a here!"
        getErrorTitle() == "Behold!"
        shouldReport()
        getCause() instanceof RuntimeException
        verifyAll(getCause()) {
          getMessage() == "WAT?!"
        }
      }
    }
  }

  def "custom path request exception"() {
    when:
    def serialized = "{\n" +
        "  \"throwable\": {\n" +
        "    \"_type\": \"com.mx.path.core.common.accessor.SomeCustomException\",\n" +
        "    \"message\": \"User dun messed up\",\n" +
        "    \"cause\": {\n" +
        "      \"_type\": \"java.lang.RuntimeException\",\n" +
        "      \"message\": \"WAT?!\"\n" +
        "    },\n" +
        "    \"report\": true,\n" +
        "    \"_fallbackType\": \"com.mx.path.core.common.serialization.PathRequestSerializableException\",\n" +
        "    \"userMessage\": \"Git out\\u0027a here!\",\n" +
        "    \"reason\": \"Because\",\n" +
        "    \"status\": 202,\n" +
        "    \"code\": \"411\",\n" +
        "    \"errorTitle\": \"Behold!\"\n" +
        "  }\n" +
        "}\n"

    SystemSerializationType deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    serialized != null
    deserialized != null
    verifyAll(deserialized) {
      getThrowable() instanceof PathRequestSerializableException
      verifyAll((PathRequestException) getThrowable()) {
        getCode() == "411"
        getStatus() == PathResponseStatus.ACCEPTED
        getMessage() == "User dun messed up"
        getUserMessage() == "Git out'a here!"
        getErrorTitle() == "Behold!"
        shouldReport()
      }
    }
  }

  def "custom path system exception"() {
    when:
    def serialized = "{\n" +
        "  \"throwable\": {\n" +
        "    \"_type\": \"com.mx.path.core.common.accessor.SomeCustomException\",\n" +
        "    \"message\": \"User dun messed up\",\n" +
        "    \"cause\": {\n" +
        "      \"_type\": \"java.lang.RuntimeException\",\n" +
        "      \"message\": \"WAT?!\"\n" +
        "    },\n" +
        "    \"report\": true,\n" +
        "    \"_fallbackType\": \"com.mx.path.core.common.serialization.PathSystemSerializableException\",\n" +
        "    \"userMessage\": \"Git out\\u0027a here!\",\n" +
        "    \"reason\": \"Because\",\n" +
        "    \"status\": 202,\n" +
        "    \"code\": \"411\",\n" +
        "    \"errorTitle\": \"Behold!\"\n" +
        "  }\n" +
        "}\n"

    SystemSerializationType deserialized = subject.fromJson(serialized, SystemSerializationType)

    then:
    serialized != null
    deserialized != null
    verifyAll(deserialized) {
      getThrowable() instanceof PathSystemSerializableException
      verifyAll((PathSystemException) getThrowable()) {
        getMessage() == "User dun messed up"
        getCause() instanceof RuntimeException
        verifyAll(getCause()) {
          getMessage() == "WAT?!"
        }
      }
    }
  }
}
