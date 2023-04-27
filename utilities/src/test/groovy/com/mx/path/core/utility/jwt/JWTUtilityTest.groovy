package com.mx.path.core.utility.jwt

import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime

import spock.lang.Specification

class JWTUtilityTest extends Specification  {

  def "fun test create JWT method"(){
    when:
    String jwt = JWTUtility.generateNewJWT("userID", "sessionID", "ClientName", "secret", 1)

    then:
    jwt.split('\\.').size() == 3
  }

  def "getClaimFromIdToken should return default value"(){
    when:
    def idToken = JWTUtility.generateNewJWT("userID", "sessionID", "ClientName", "secret", 1)
    def claim = JWTUtility.getClaimFromIdToken(idToken, "blah", String.class, "default");

    then:
    claim == "default"
  }

  def "validating a proper JWT with passing wrong client name"(){
    when:
    String jwt = JWTUtility.generateNewJWT("userID", "sessionID", "ClientName", "secret", 1)

    then: "Only valid with correct issuer"
    !JWTUtility.isValidJWT(jwt, "secret", "")
    JWTUtility.isValidJWT(jwt, "secret", "ClientName")
  }

  def "validating a tampered JWT"(){
    when:
    String tamperedJWT = JWTUtility.generateNewJWT("userID", "sessionID", "ClientName", "tampered", 1)

    then: "Not valid with non-matching issuer"
    !JWTUtility.isValidJWT(tamperedJWT, "tamperedJWT", "ClientName")
  }

  def "seconds to expiration"() {
    given:
    ZonedDateTime zonedDateTime = ZonedDateTime.of(
        2021,
        7,
        22,
        9,
        30,
        0,
        0,
        ZoneId.of("America/Chicago"))
    Date date = Date.from(zonedDateTime.toInstant())

    when:
    String jwt = JWTUtility.generateNewJWT(
        "userId",
        "sessionId",
        "Client Name",
        "secret",
        date,
        Date.from(date.toInstant().plusMillis(2000)))

    then: "seconds to expiration is positive"
    JWTUtility.secondsToExpiration(jwt, date) == 2

    when: "expire the token"
    def expired = Date.from(date.toInstant().plusMillis(3000))

    then: "seconds to expiration is negative"
    JWTUtility.secondsToExpiration(jwt, expired) == -1

    when: "jwt is messed up"
    jwt = Base64.encoder.encodeToString("{ invalid : 'jwt' }".getBytes(Charset.defaultCharset()))

    then: "returns 0"
    JWTUtility.secondsToExpiration(jwt) == 0

    when: "should expire in 2 hours"
    jwt = JWTUtility.generateNewJWT(
        "userId",
        "sessionId",
        "Client Name",
        "secret",
        date,
        Date.from(date.toInstant().plusMillis(7200000)))
    def secondsToExpiration = JWTUtility.secondsToExpiration(jwt, date)

    then:
    secondsToExpiration == 7200
  }

  def "is expired"() {
    when:
    String jwt = JWTUtility.generateNewJWT("userId", "sessionId", "Client Name", "secret", 2)

    then: "is not expired"
    !JWTUtility.isExpired(jwt)

    when: "expire the token"
    Thread.sleep(2000)

    then: "is expired"
    JWTUtility.isExpired(jwt)
  }

  def "getClaimFromIdToken"() {
    given:
    String idToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwidGVzdEJvb2xlYW4iOnRydWUsInRlc3REb3VibGUiOjIzLjQ1LCJpYXQiOjE1MTYyMzkwMjJ9.lWQ7KT4kb8zs9yb5mf1jgWYgo4Zv1mOcdKfT44jlkUg"

    when:
    String claimAud = JWTUtility.getClaimFromIdToken(idToken, "sub", String.class, "")

    then:
    claimAud == "1234567890"

    when:
    int claimAuthTime = JWTUtility.getClaimFromIdToken(idToken, "iat", Integer.class, 0)

    then:
    claimAuthTime == 1516239022

    when:
    boolean claimTestBoolean = JWTUtility.getClaimFromIdToken(idToken, "testBoolean", Boolean.class, false)

    then:
    claimTestBoolean

    when:
    Double claimTestDouble = JWTUtility.getClaimFromIdToken(idToken, "testDouble", Double.class, new Double(0.0))

    then:
    claimTestDouble == 23.45
  }
}
