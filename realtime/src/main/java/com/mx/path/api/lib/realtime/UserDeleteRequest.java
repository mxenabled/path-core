package com.mx.path.api.lib.realtime;

import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.gateway.net.Request;

public class UserDeleteRequest extends Request {

  // Public

  public UserDeleteRequest(String baseUrl, String clientId, String apiKey, String userId) {
    setupRequest(baseUrl, clientId, apiKey, userId);
  }

  // Private

  /**
   * setup the request using the member variables
   */
  private void setupRequest(String baseUrl, String clientId, String apiKey, String userId) {
    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("origination")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + ".xml")
        .withMethod("DELETE")
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          if (status != HttpStatus.NO_CONTENT) {
            throw new RuntimeException("Error deleting Mdx user");
          }
        });
  }
}
