package com.mx.common.configuration

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.testing.serialization.ConfigurationWithSecrets

import spock.lang.Specification

class ConfigurationSerializerTest extends Specification {
  Gson subject

  void setup() {
    subject = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(new ConfigurationSerializer.Factory())
        .create()
  }

  def "Serialize"() {
    when:
    def original = ConfigurationWithSecrets.builder().defaults().build();

    def serialized = subject.toJson(original)
    def deserialized = subject.fromJson(serialized, ConfigurationWithSecrets.class)

    then:
    verifyAll {
      !serialized.contains("skipped")
      deserialized.secret == "****"
      deserialized.notSecret == original.notSecret
      deserialized.pinteger == original.pinteger
      deserialized.cinteger == original.cinteger
      deserialized.plong == original.plong
      deserialized.clong == original.clong
      deserialized.number.toLong() == original.number.toLong()
      deserialized.pdouble == original.pdouble
      deserialized.cdouble == original.cdouble
      deserialized.pboolean == original.pboolean
      deserialized.cboolean == original.cboolean
      deserialized.enumeration == original.enumeration
      deserialized.embedded.embeddedSecret == "****"
      deserialized.embedded.inception.whereAmI == "****"
      deserialized.embedded.inception.id == original.embedded.inception.id
    }
  }

  def "Serialize with nulls"() {
    when:
    def original = ConfigurationWithSecrets.builder()
        .defaults()
        .cboolean(null)
        .embedded(null)
        .build()

    def serialized = subject.toJson(original)
    println(serialized)
    def deserialized = subject.fromJson(serialized, ConfigurationWithSecrets.class)

    then:
    verifyAll {
      !serialized.contains("skipped")
      deserialized.secret == "****"
      deserialized.notSecret == original.notSecret
      deserialized.pinteger == original.pinteger
      deserialized.cinteger == original.cinteger
      deserialized.plong == original.plong
      deserialized.clong == original.clong
      deserialized.number.toLong() == original.number.toLong()
      deserialized.pdouble == original.pdouble
      deserialized.cdouble == original.cdouble
      deserialized.pboolean == original.pboolean
      deserialized.cboolean == null
      deserialized.enumeration == original.enumeration
      deserialized.embedded == null
    }
  }

  def "Serialize with blank secrets"() {
    when:
    def original = ConfigurationWithSecrets.builder()
        .defaults()
        .secret("")
        .build()

    def serialized = subject.toJson(original)
    println(serialized)
    def deserialized = subject.fromJson(serialized, ConfigurationWithSecrets.class)

    then:
    deserialized.secret == ""

    when:
    original = ConfigurationWithSecrets.builder()
        .defaults()
        .secret(null)
        .build()

    serialized = subject.toJson(original)
    println(serialized)
    deserialized = subject.fromJson(serialized, ConfigurationWithSecrets.class)

    then:
    deserialized.secret == null
  }
}
