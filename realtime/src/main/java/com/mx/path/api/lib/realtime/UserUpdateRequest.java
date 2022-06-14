package com.mx.path.api.lib.realtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.api.lib.realtime.models.MdxUser;
import com.mx.path.api.lib.realtime.models.MdxUserWrapper;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class UserUpdateRequest extends Request {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  // Public

  public UserUpdateRequest(String baseUrl, String clientId, String apiKey, String userId, MdxUser user) {
    setupRequest(baseUrl, clientId, apiKey, userId, user);
  }

  // Private

  /**
   * setup the request using the member variables
   */
  private void setupRequest(String baseUrl, String clientId, String apiKey, String userId, MdxUser user) {
    MdxUserWrapper mdxUserWrapper = new MdxUserWrapper();
    mdxUserWrapper.setUser(user);

    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + ".json")
        .withMethod("PUT")
        .withContentType(mdxType.toString())
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withBody(GSON.toJson(mdxUserWrapper))
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          if (status != HttpStatus.OK && status != HttpStatus.CONFLICT) {
            throw new MdxApiException("Error updating Mdx user", clientId, HttpStatus.UNAUTHORIZED, "id", "mdx_failed", true, null);
          }
        })
        .withProcessor(response -> GSON.fromJson(response.getBody(), MdxUserWrapper.class));
  }
}
