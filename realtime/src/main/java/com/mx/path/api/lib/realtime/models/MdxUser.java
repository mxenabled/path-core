package com.mx.path.api.lib.realtime.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.mx.common.collections.ObjectMap;

@Data
public class MdxUser {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  @SerializedName("id")
  private String id;
  @SerializedName("guid")
  private String guid;
  @SerializedName("birthdate")
  private String birthdate;
  @SerializedName("credit_score")
  private String creditScore;
  @SerializedName("email")
  private String email;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("gender")
  private String gender;
  @SerializedName("is_disabled")
  private Boolean isDisabled;
  @SerializedName("logged_in_at")
  private Long loggedInAt;
  @SerializedName("metadata")
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private String metadata;
  @SerializedName("phone")
  private String phone;
  @SerializedName("zip_code")
  private String zipCode;

  public final String getMetadataField(String key) {
    return getMetadataField(key, String.class);
  }

  public final <T> T getMetadataField(String key, Class<T> klass) {
    if (metadata != null) {
      ObjectMap metadataMap = GSON.fromJson(metadata, ObjectMap.class);
      if (metadataMap.isNotNull(key)) {
        return metadataMap.getAs(klass, key);
      }
    }
    return null;
  }

  public final <T> void setMetadataField(String key, T value) {
    ObjectMap metadataMap = (metadata == null) ? new ObjectMap() : GSON.fromJson(metadata, ObjectMap.class);
    metadataMap.put(key, value);
    metadata = GSON.toJson(metadataMap, ObjectMap.class);
  }
}
