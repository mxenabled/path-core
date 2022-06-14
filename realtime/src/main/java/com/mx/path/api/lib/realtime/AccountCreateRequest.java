package com.mx.path.api.lib.realtime;

import lombok.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.api.lib.realtime.models.MdxAccount;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class AccountCreateRequest extends Request {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public AccountCreateRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId, MdxAccount account) {
    setupRequest(baseUrl, clientId, apiKey, userId, memberId, account);
  }

  // public

  /**
   * setup the request using the member variables
   */
  public void setupRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId, MdxAccount account) {
    MdxAccountWrapper mdxAccountWrapper = new MdxAccountWrapper();
    mdxAccountWrapper.setAccount(account);

    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + "/members/" + memberId + "/accounts.json")
        .withMethod("POST")
        .withContentType(mdxType.toString())
        .withAccept(mdxType.toString())
        .withHeaders(headers -> {
          headers.put("Accept-Encoding", "gzip, deflate, br");
          headers.put("MD-API-KEY", apiKey);
        })
        .withBody(GSON.toJson(mdxAccountWrapper))
        .withOnComplete(response -> {
          HttpStatus status = response.getStatus();
          // if the member_id doesn't exist we should have a successful creation and expect a 200 status;
          // if the member_id already exists we would expect a 409 status;
          // other status would indicate something might have gone wrong.
          if (status != HttpStatus.OK && status != HttpStatus.CONFLICT) {
            throw new MdxApiException("Error checking/creating Mdx account", clientId, HttpStatus.UNAUTHORIZED, "id", "mdx_failed", true, null);
          }
        });
  }

  // wrapper class for MdxUser
  @Data
  public static class MdxAccountWrapper {
    @SerializedName("account")
    private MdxAccount account;
  }
}
