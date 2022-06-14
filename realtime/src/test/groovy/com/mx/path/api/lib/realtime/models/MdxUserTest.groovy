package com.mx.path.api.lib.realtime.models

import com.mx.path.api.connect.messaging.remote.models.RemoteUser

import spock.lang.Specification
import spock.lang.Unroll

class MdxUserTest extends Specification {

  def "test for getter/setter"() {
    given:
    def user = new MdxUser()

    when:
    user.setId("id")
    user.setGuid("guid")
    user.setBirthdate("12-12-1990")
    user.setCreditScore("781")
    user.setEmail("user@company.com")
    user.setFirstName("first_name")
    user.setLastName("last_name")
    user.setGender("Female")
    user.setIsDisabled(false)
    user.setLoggedInAt(12345L)
    user.setPhone("111-333-3456")
    user.setZipCode("12345-5678")

    then:
    user.getId() == "id"
    user.getGuid() == "guid"
    user.getBirthdate() == "12-12-1990"
    user.getCreditScore() == "781"
    user.getEmail() == "user@company.com"
    user.getFirstName() == "first_name"
    user.getLastName() == "last_name"
    user.getGender() == "Female"
    !user.getIsDisabled()
    user.getLoggedInAt() == 12345L
    user.getPhone() == "111-333-3456"
    user.getZipCode() == "12345-5678"
  }

  def "getMetadataField for non-existent field is null"() {
    given:
    def mdxUser = new MdxUser()

    when:
    def result = mdxUser.getMetadataField("something", String)

    then:
    result == null
  }

  def "getMetadataField String"() {
    given:
    def fingerprint = "U-12345"
    def mdxUser = new MdxUser().tap { setMetadataField("fingerprint", fingerprint) }

    when:
    def result = mdxUser.getMetadataField("fingerprint")

    then:
    result == fingerprint
  }

  @Unroll
  def "getMetadataField for #klass - #value"() {
    given:
    def mdxUser = new MdxUser().tap { setMetadataField("someValue", value) }

    expect:
    mdxUser.getMetadataField("someValue", klass) == value

    where:
    klass   || value
    Boolean || true
    Integer || 1
    Double  || 2.0
    String  || "hello"
  }

  def "getMetadataField for complex object"() {
    given:
    def remoteUser = new RemoteUser().tap { setLastName("Doe")}
    def mdxUser = new MdxUser().tap { setMetadataField("someValue", remoteUser) }

    when:
    def result = mdxUser.getMetadataField("someValue", RemoteUser)

    then:
    result.lastName == "Doe"
  }

  def "setMetadataField different key does not overwrite existing metadata field"() {
    given:
    def fingerprint = "U-12345"
    def mdxUser = new MdxUser().tap {
      setMetadataField("fingerprint", fingerprint)
      setMetadataField("anotherValue", "don't blow me away")
    }

    when:
    def result = mdxUser.getMetadataField("fingerprint", String)

    then:
    result == fingerprint
  }

  def "setMetadataField same key overwrites existing metadata field"() {
    given:
    def fingerprint = "U-12345"
    def newFingerprint = "don't blow me away"
    def mdxUser = new MdxUser().tap {
      setMetadataField("fingerprint", fingerprint)
      setMetadataField("fingerprint", newFingerprint)
    }

    when:
    def result = mdxUser.getMetadataField("fingerprint", String)

    then:
    result == newFingerprint
  }
}
