package com.mx.path.api.lib.realtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.api.lib.realtime.models.MdxUserWrapper;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class UserGetRequest extends Request {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  // Public

  public UserGetRequest(String baseUrl, String clientId, String apiKey, String userId) {
    setupRequest(baseUrl, clientId, apiKey, userId);
  }

  // Private

  /**
   * setup the request using the member variables
   */
  private void setupRequest(String baseUrl, String clientId, String apiKey, String userId) {
    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + ".json")
        .withMethod("GET")
        .withContentType(mdxType.toString())
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          if (status != HttpStatus.OK) {
            throw new MdxApiException("Error retrieving Mdx user", clientId, HttpStatus.NOT_FOUND, "id", "mdx_failed", true, null);
          }
        })
        .withProcessor(response -> GSON.fromJson(response.getBody(), MdxUserWrapper.class));
  }
}
