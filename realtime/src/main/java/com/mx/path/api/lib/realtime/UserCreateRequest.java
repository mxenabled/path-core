package com.mx.path.api.lib.realtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.common.lang.Strings;
import com.mx.path.api.connect.messaging.remote.models.RemoteUser;
import com.mx.path.api.lib.realtime.models.MdxUser;
import com.mx.path.api.lib.realtime.models.MdxUserWrapper;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class UserCreateRequest extends Request {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  // Public

  public UserCreateRequest(String baseUrl, String clientId, String apiKey, String userId) {
    setupRequest(baseUrl, clientId, apiKey, userId, null);
  }

  public UserCreateRequest(String baseUrl, String clientId, String apiKey, String userId, RemoteUser user) {
    setupRequest(baseUrl, clientId, apiKey, userId, user);
  }

  // Private

  /**
   * setup the request using the member variables
   */
  private void setupRequest(String baseUrl, String clientId, String apiKey, String userId, RemoteUser user) {
    MdxUser mdxUser = new MdxUser();
    mdxUser.setId(userId);
    if (user != null) {
      if (Strings.isNotBlank(user.getFirstName())) {
        mdxUser.setFirstName(user.getFirstName());
      }
      if (Strings.isNotBlank(user.getEmail())) {
        mdxUser.setEmail(user.getEmail());
      }
      if (Strings.isNotBlank(user.getLastName())) {
        mdxUser.setLastName(user.getLastName());
      }
      if (Strings.isNotBlank(user.getPhone())) {
        mdxUser.setPhone(user.getPhone());
      }
    }

    MdxUserWrapper mdxUserWrapper = new MdxUserWrapper();
    mdxUserWrapper.setUser(mdxUser);

    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users.json")
        .withMethod("POST")
        .withContentType(mdxType.toString())
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withBody(GSON.toJson(mdxUserWrapper))
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          // if the user_id doesn't exist we create one and expect a 200 status;
          // if the user_id already exists we expect a 409 status;
          // other status would indicate something might have gone wrong.
          if (status != HttpStatus.OK && status != HttpStatus.CONFLICT) {
            throw new MdxApiException("Error checking/creating Mdx user", clientId, HttpStatus.UNAUTHORIZED, "id", "mdx_failed", true, null);
          }
        })
        .withProcessor(response -> GSON.fromJson(response.getBody(), MdxUserWrapper.class));
  }
}
