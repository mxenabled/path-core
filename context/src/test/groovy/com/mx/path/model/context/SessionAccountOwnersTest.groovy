package com.mx.path.model.context


import spock.lang.Specification

class SessionAccountOwnersTest extends Specification {
  SessionAccountOwner subject

  def setup() {
    subject = new SessionAccountOwner()
  }

  def "setsCommaSeparatedFullName"() {
    given:
    subject.commaSeparatedFullName("Dent,Arthur")

    expect:
    "Arthur" == subject.getFirstName()
    "Dent" == subject.getLastName()
  }

  def "setsCommaSeparatedFullNameTrims"() {
    given:
    subject.commaSeparatedFullName(" Prefect , Ford ")

    expect:
    "Ford" == subject.getFirstName()
    "Prefect" == subject.getLastName()
  }

  def "doesNotFailOnIncompleteFullName"() {
    given:
    subject.commaSeparatedFullName(name)

    expect:
    subject.getFirstName() == first
    subject.getLastName() == last

    where:
    name        | first | last
    "McMillan," | null  | "McMillan"
    "Marvin"    | null  | "Marvin"
  }

  def "commaSeparatedFullNameBlank"() {
    given:
    subject.commaSeparatedFullName("")

    expect:
    null == subject.getFirstName()
    null == subject.getLastName()
  }

  def "commaSeparatedFullNameNull"() {
    given:
    subject.commaSeparatedFullName(null)

    expect:
    null == subject.getFirstName()
    null == subject.getLastName()
  }
}