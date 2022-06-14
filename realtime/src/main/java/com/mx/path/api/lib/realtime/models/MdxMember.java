package com.mx.path.api.lib.realtime.models;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class MdxMember {
  @SerializedName("id")
  private String id;
  @SerializedName("guid")
  private String guid;
  @SerializedName("is_disabled")
  private Boolean isDisabled;
  @SerializedName("institution_id")
  private String institutionId;
  @SerializedName("metadata")
  private String metadata;
  @SerializedName("name")
  private String name;
  @SerializedName("user_guid")
  private String userGuid;
  @SerializedName("user_id")
  private String userId;
  @SerializedName("login")
  private String login;
  @SerializedName("password")
  private String password;
  @SerializedName("userkey")
  private String userKey;
}
