package com.mx.path.api.lib.realtime.models;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class MdxUserWrapper {
  @SerializedName("user")
  private MdxUser user;
}
