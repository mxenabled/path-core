package com.mx.models.challenges;

import com.mx.models.MdxBase;

public final class Image extends MdxBase<Image> {
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
