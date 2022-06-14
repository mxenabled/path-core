package com.mx.models.challenges;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class DeepLinkData extends MdxBase<DeepLinkData> {

  @SerializedName("deep_link")
  private String deepLink;

  public String getDeepLink() {
    return deepLink;
  }

  public void setDeepLink(String deepLink) {
    this.deepLink = deepLink;
  }
}
