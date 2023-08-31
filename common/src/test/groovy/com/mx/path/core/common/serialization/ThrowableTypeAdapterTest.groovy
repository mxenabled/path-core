package com.mx.path.core.common.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.accessor.UnauthorizedException
import com.mx.testing.serialization.SystemSerializationType

import spock.lang.Specification

class ThrowableTypeAdapterTest extends Specification {
  Gson subject

  def setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeHierarchyAdapter(Throwable.class, new ThrowableTypeAdapter())
        .registerTypeAdapterFactory(SystemTypeAdapterFactory.builder().build())
        .create()
  }

  def "handles null"() {
    given:
    SystemSerializationType target = SystemSerializationType.builder()
        .throwable(null)
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    println(serialized)

    then:
    deserialized.throwable == null
  }

  def "handles without cause"() {
    given:
    def throwable = new UnauthorizedException("Unauthorized", "Wrong password")

    SystemSerializationType target = SystemSerializationType.builder()
        .throwable(throwable)
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    println(serialized)

    then:
    deserialized.throwable != null
    deserialized.throwable instanceof UnauthorizedException
    deserialized.throwable.cause == null
  }

  def "handles with cause"() {
    given:
    def cause = new IllegalArgumentException("Password")
    def throwable = new UnauthorizedException("Unauthorized", "Wrong password", cause)

    SystemSerializationType target = SystemSerializationType.builder()
        .throwable(throwable)
        .build()

    when:
    def serialized = subject.toJson(target)
    def deserialized = subject.fromJson(serialized, SystemSerializationType)

    println(serialized)

    then:
    deserialized.throwable != null
    deserialized.throwable instanceof UnauthorizedException
    deserialized.throwable.cause != null
    deserialized.throwable.cause instanceof IllegalArgumentException
  }

  def "common path exception"() {
    given:
    def unauthorizedException
    try {
      try {
        throw new RuntimeException("Password check failed")
      } catch (RuntimeException e) {
        throw new UnauthorizedException("Authentication failed", "Password incorrect", e)

        .withStatus(PathResponseStatus.ACCEPTED)
        .withCode("4001")
        .withErrorTitle("Username/password incorrect")
        .withReason("Password")
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
        getCode() == "4001"
        getStatus() == PathResponseStatus.ACCEPTED
        getMessage() == "Authentication failed"
        getUserMessage() == "Password incorrect"
        getErrorTitle() == "Username/password incorrect"
        shouldReport()
        getCause() instanceof RuntimeException
        verifyAll(getCause()) {
          getMessage() == "Password check failed"
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
        "    \"status\": ACCEPTED,\n" +
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
      verifyAll((PathRequestSerializableException) getThrowable()) {
        getCode() == "411"
        getStatus() == PathResponseStatus.ACCEPTED
        getMessage() == "User dun messed up"
        getOriginalType() == "com.mx.path.core.common.accessor.SomeCustomException"
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
        "    \"status\": ACCEPTED,\n" +
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
      verifyAll((PathSystemSerializableException) getThrowable()) {
        getCause() instanceof RuntimeException
        getMessage() == "User dun messed up"
        getOriginalType() == "com.mx.path.core.common.accessor.SomeCustomException"
        verifyAll(getCause()) {
          getMessage() == "WAT?!"
        }
      }
    }
  }
}
