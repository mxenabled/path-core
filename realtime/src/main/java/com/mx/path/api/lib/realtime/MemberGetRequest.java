package com.mx.path.api.lib.realtime;

import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class MemberGetRequest extends Request {

  public MemberGetRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId) {
    setupRequest(baseUrl, clientId, apiKey, userId, memberId);
  }

  // public

  /**
   * setup the request using the member variables
   */
  public void setupRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId) {
    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + "/members/" + memberId + ".json")
        .withMethod("GET")
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          if (status != HttpStatus.OK) {
            throw new MdxApiException("Error getting Mdx member", clientId, status, "id", "mdx_failed", false, null);
          }
        });
  }

}
