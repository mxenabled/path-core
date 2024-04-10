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

  def "Test getAllClaimsFromIdToken method with Map<String, String> return type"() {
    given:
    def idToken = "eyJraWQiOiJmMFI5d0lnbmtBTmJsNWFaWERQYy00OFhiRjlhdkhuZ0NsTWpvYlVacnBnIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHUxdmo3MTQyY0FuM3psNTBoOCIsInZlciI6MSwiaXNzIjoiaHR0cHM6Ly9oYW5jb2Nrd2hpdG5leS1zaXQub2t0YXByZXZpZXcuY29tL29hdXRoMi9kZWZhdWx0IiwiYXVkIjoiMG9hMXM0MTF0MXIzMElGMjMwaDgiLCJpYXQiOjE3MTI3NjY2ODAsImV4cCI6MTcxMjc3MDI4MCwianRpIjoiSUQuM01GMDA3czFPZlIyNGFza2lmcmRTSWxZeXFnVnBqUXFjUXkyNVg4c2FjMCIsImFtciI6WyJwd2QiXSwiaWRwIjoiMDBvcGJlaDl2OXJNaFVSeDkwaDciLCJhdXRoX3RpbWUiOjE3MTI3NjY2NzgsImF0X2hhc2giOiJoM0ZxcDZURTF0bElxcUtMckR6bTdnIiwiaHR0cDovL2V6c2Rldi5uZXQvY2xhaW1zL3BhY2thZ2VfaWQvRElTQUJMRUQiOiJIV0IwMDAwMSIsImxhc3ROYW1lIjoiUE9PTCIsImZpcnN0TmFtZSI6IkRPTkFMRCIsImV4dGVybmFsSUQiOiIwMDAwMDAyMDI2OTc2NyIsInVzZXJUeXBlIjoiUGVyc29uIiwibG9naW4iOiJzaXR0ZXN0ZXI0IiwidXNlcklkIjoiMDB1MXZqNzE0MmNBbjN6bDUwaDgiLCJwcmltYXJ5RW1haWwiOiJteC10ZXN0LWVtYWlsQGZuaXMuY29tIiwidHlwZUNvZGUiOiI3In0.ThNRon2OIjLxnMzeoDMx7Qf9pN0hyiaV3an9OPW17JByWR37KGRI3TJ4adxXCDHOUsvHgFn5y6NoM53kJ6O0XdrPrhub9H5b_0DfuCcqrX34EtOj73ozdrQ4gpsV-q1YSTm3wIPidO_je5DUPgef5yZAoJ8w658oeOuh_Uo30PyB1g76pTVNxFvvZHcsb8doq3N-Sh-jKvytXYA_6f2bRWCKzw7dREIWx7ezsR7Nc_5e3bmLpFpMNMHFi2TsUoFWv0my49d_neMM1rcMxjljrYHK1MtG3XLYCLi_lR2VGfm6Kk86FhOijEe38rKwqkxjjIjSE_25SO0wc38amXcv-g"

    when:
    def claimsMap = JWTUtility.getAllClaimsFromIdToken(idToken)

    then:
    claimsMap.size() == 20
    claimsMap.sub == "00u1vj7142cAn3zl50h8"
    claimsMap.typeCode == "7"
  }
}
