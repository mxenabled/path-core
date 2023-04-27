package com.mx.path.core.utility.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtility {

  private static final long ONE_SECOND_IN_MILLIS = 1000;

  /**
   * Generates a new JWT
   * @param userID Subject
   * @param sessionId Session ID
   * @param clientName Issuer
   * @param secret Client Secret
   * @param secondsToExpiration Number of seconds the JWT should be valid
   * @return Signed JWT
   */
  public static String generateNewJWT(String userID, String sessionId, String clientName, String secret, long secondsToExpiration) {
    Date iss = new Date();
    return generateNewJWT(userID, sessionId, clientName, secret, iss, new Date(iss.getTime() + secondsToExpiration * ONE_SECOND_IN_MILLIS));
  }

  /**
   * Performs full validation of given JWT
   * @param token JWT
   * @param secret Client Secret
   * @param clientName Issuer
   * @return true, if the JWT is valid.
   */
  public static boolean isValidJWT(String token, String secret, String clientName) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
          .withIssuer(clientName)
          .build(); // Reusable verifier instance
      verifier.verify(token);

      return true;
    } catch (JWTVerificationException exception) {
      return false;
    }
  }

  /**
   * @param token JWT
   * @return Number of Seconds until token expiration
   */
  public static long secondsToExpiration(String token) {
    return secondsToExpiration(token, new Date());
  }

  /**
   * @param token JWT
   * @return true, if the token expiration is in the past
   */
  public static boolean isExpired(String token) {
    return secondsToExpiration(token) <= 0;
  }

  /**
   * @param token JWT
   * @return Number of Seconds until token expiration
   */
  static long secondsToExpiration(String token, Date date) {
    try {
      DecodedJWT decodeJWT = JWT.decode(token);

      return (decodeJWT.getExpiresAt().getTime() - date.getTime()) / ONE_SECOND_IN_MILLIS;
    } catch (JWTDecodeException exception) {
      return 0;
    }
  }

  /**
   * Generates a new JWT
   * @param userID Subject
   * @param sessionId Session ID
   * @param clientName Issuer
   * @param secret Client Secret
   * @param expiration The date it should expire
   * @return Signed JWT
   */
  static String generateNewJWT(String userID, String sessionId, String clientName, String secret, Date issued, Date expiration) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
        .withSubject(userID)
        .withClaim("sessionId", sessionId)
        .withIssuer(clientName)
        .withExpiresAt(expiration)
        .withIssuedAt(issued)
        .sign(algorithm);
  }

  public static <T> T getClaimFromIdToken(String idToken, String key, Class<T> returnType, T defaultValue) {
    DecodedJWT idDecodedToken = JWT.decode(idToken);
    Claim claim = idDecodedToken.getClaim(key);
    if (claim.isMissing() || claim.isNull()) {
      return defaultValue;
    }
    return claim.as(returnType);
  }
}
