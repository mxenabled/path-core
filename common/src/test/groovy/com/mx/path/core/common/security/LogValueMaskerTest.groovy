package com.mx.path.core.common.security


import spock.lang.Specification

class LogValueMaskerTest extends Specification {

  LogValueMasker subject

  def setup() {
    subject = new LogValueMasker()
  }

  def cleanup() {
    LogValueMasker.resetPatterns()
  }

  def "keepsNonSensitiveHeaderValue"() {
    expect:
    "cares" == subject.maskHeaderValue("nobody", "cares")
  }

  def "replacesSensitiveHeaderValues"() {
    expect:
    "**MASKED**" == subject.maskHeaderValue("x-csrf-token", "superSensitive")
    "**MASKED**" == subject.maskHeaderValue("X-CSRF-TOKEN", "superSensitive")
  }

  def "masksAntiForgeryTokenInJsonStringBody"() {
    given:
    def json = "{`antiForgeryToken`:`sensitive`,`user_id`:`bob`}".replace('`', '"')

    expect:
    "{`antiForgeryToken`:`**MASKED**`,`user_id`:`bob`}".replace('`', '"') == subject.maskPayload(json)
  }

  def "masksPasswordInJsonStringBody"() {
    given:
    def json = "{`authentication`:{`username`:`frank`,`password`:`\$up3r\$3cr4t`}}".replace('`', '"');

    expect:
    "{`authentication`:{`username`:`frank`,`password`:`**MASKED**`}}".replace('`', '"') == subject.maskPayload(json)
  }

  def "masksMultipleOccurrencesInJsonStringBody"() {
    given:
    def json = "{`token`:`SecretStuff`,`token`:`\$up3r\$3cr4t`}".replace('`', '"')

    expect:
    "{`token`:`**MASKED**`,`token`:`**MASKED**`}".replace('`', '"') == subject.maskPayload(json)
  }

  def "registerPayloadPattern"() {
    given:
    LogValueMasker.registerPayloadPattern("(Secret)")

    expect:
    "Some**MASKED**Stuff" == subject.maskPayload("SomeSecretStuff")
  }

  def "registerHeaderKey"() {
    given:
    LogValueMasker.registerHeaderKey("secret")

    expect:
    "**MASKED**" == subject.maskHeaderValue("secret", "superSensitive")
  }

  def "registerCookieKey"() {
    given:
    LogValueMasker.registerCookieKey("secret")

    expect:
    "secret=**MASKED**" == subject.maskHeaderValue("Cookie", "secret=superSensitive")
  }

  def "registerJsonPayloadPattern"() {
    given:
    LogValueMasker.registerPayloadPattern("\"password\":\"(.+)\"")

    expect:
    "\"password\":\"**MASKED**\"" == subject.maskPayload("\"password\":\"SOME_PASSWORD\"")
  }

  def "registerJsonPayloadPatternWithSpaces"() {
    given:
    LogValueMasker.registerPayloadPattern("\"password\" : \"(.+)\"")

    expect:
    "\"password\" : \"**MASKED**\"" == subject.maskPayload("\"password\" : \"SOME_PASSWORD\"")
  }

  def "registerJsonPayloadPatternWithMultipleMatches"() {
    given:
    LogValueMasker.registerPayloadPattern("\"refreshToken\":\\s?\"([^.]*)\\.[^.]*\\.([^.]*)\"")

    expect:
    "\"refreshToken\":\"**MASKED**.payload.**MASKED**\"" == subject.maskPayload("\"refreshToken\":\"header.payload.signature\"")
  }

  def "registerJsonPayloadPatternWithMultipleMatches (single match)"() {
    given:
    LogValueMasker.registerPayloadPattern("password=((.+)(?=&)|(.+))")

    expect:
    "password=**MASKED**" == subject.maskPayload("password=sensitive")
  }

  def "registerJsonPayloadPatternWithMultipleMatches (no matches)"() {
    given:
    LogValueMasker.registerPayloadPattern("password=((.+)(?=&)|(.+))")

    expect:
    "something=different" == subject.maskPayload("something=different")
  }
}
