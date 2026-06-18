package com.mx.path.api.connect.http.certificate

import com.mx.path.connect.http.certificate.FieldSettingsValidationError

import spock.lang.Specification

class FieldSettingsValidationErrorTest extends Specification {

  def "string constructor stores message"() {
    given:
    def ex = new FieldSettingsValidationError("something went wrong")

    expect:
    ex.getMessage() == "something went wrong"
  }

  def "list constructor stores fields and retrieves them"() {
    given:
    def field = new FieldSettingsValidationError.Field("keystorePath", "must not be empty")
    def ex = new FieldSettingsValidationError([field])

    expect:
    ex.getFields().size() == 1
    ex.getFields()[0].is(field)
  }

  def "Field constructor, getters and setters"() {
    given:
    def field = new FieldSettingsValidationError.Field("keystorePath", "must not be empty")

    expect:
    field.getField() == "keystorePath"
    field.getError() == "must not be empty"

    when:
    field.setField("keystorePassword")
    field.setError("must not be null")

    then:
    field.getField() == "keystorePassword"
    field.getError() == "must not be null"
  }

  def "setFields replaces the fields list"() {
    given:
    def f1 = new FieldSettingsValidationError.Field("a", "err1")
    def f2 = new FieldSettingsValidationError.Field("b", "err2")
    def ex = new FieldSettingsValidationError([f1])

    when:
    ex.setFields([f1, f2])

    then:
    ex.getFields().size() == 2
  }
}
