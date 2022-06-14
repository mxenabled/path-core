package com.mx.path.api.lib.realtime;

import lombok.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.mx.common.http.HttpStatus;
import com.mx.common.http.MediaType;
import com.mx.path.api.lib.realtime.models.MdxMember;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.util.MdxApiException;

public class MemberCreateRequest extends Request {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public MemberCreateRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId, String userkey) {
    setupRequest(baseUrl, clientId, apiKey, userId, buildMember(memberId, userkey));
  }

  public MemberCreateRequest(String baseUrl, String clientId, String apiKey, String userId, String memberId, String login, char[] password) {
    setupRequest(baseUrl, clientId, apiKey, userId, buildMember(memberId, login, password));
  }

  // public

  /**
   * setup the request using the member variables
   */
  public void setupRequest(String baseUrl, String clientId, String apiKey, String userId, MdxMember mdxMember) {
    MdxMemberWrapper mdxUserWrapper = new MdxMemberWrapper();
    mdxUserWrapper.setMember(mdxMember);

    MediaType mdxType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json");

    this.withFeatureName("id")
        .withBaseUrl(baseUrl)
        .withPath("/" + clientId + "/users/" + userId + "/members.json")
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
          // if the member_id doesn't exist we should have a successful creation and expect a 200 status;
          // if the member_id already exists we would expect a 409 status;
          // other status would indicate something might have gone wrong.
          if (status != HttpStatus.OK && status != HttpStatus.CONFLICT) {
            throw new MdxApiException("Error checking/creating Mdx member", clientId, HttpStatus.UNAUTHORIZED, "id", "mdx_failed", true, null);
          }
        });
  }

  // wrapper class for MdxUser
  @Data
  public static class MdxMemberWrapper {
    @SerializedName("member")
    private MdxMember member;
  }

  private MdxMember buildMember(String memberId, String login, char[] password) {
    MdxMember mdxMember = new MdxMember();
    mdxMember.setId(memberId);
    mdxMember.setLogin(login);
    mdxMember.setPassword(new String(password));

    return mdxMember;
  }

  private MdxMember buildMember(String memberId, String userkey) {
    MdxMember mdxMember = new MdxMember();
    mdxMember.setId(memberId);
    mdxMember.setUserKey(userkey);

    return mdxMember;
  }
}
