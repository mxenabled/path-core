package com.mx.path.core.utility.oauth;

import java.nio.charset.Charset;
import java.util.Base64;

public class OAuthUtility {

  /**
   * Generates Authorization Token
   *
   * @param clientId     Id of the client
   * @param clientSecret Client's Secret
   * @return Authorization Token
   */
  public static String generateBasicAuthorizationToken(String clientId, String clientSecret) {
    return clientId != null && clientId.length() > 0 && clientSecret != null && clientSecret.length() > 0
        ? "Basic " + Base64.getEncoder().encodeToString((clientId
            + ":" + clientSecret).getBytes(Charset.defaultCharset()))
        : "";
  }
}
