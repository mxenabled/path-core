package com.mx.models.id;

import com.mx.models.MdxBase;

public class MfaChallengeOption extends MdxBase<MfaChallengeOption> {
  private String id;
  private String name;

  public final String getId() {
    return id;
  }

  public final void setId(String id) {
    this.id = id;
  }

  public final String getName() {
    return name;
  }

  public final void setName(String name) {
    this.name = name;
  }
}
