package com.mx.models.challenges;

import com.mx.models.MdxBase;

public final class Option extends MdxBase<Option> {
  private String id;
  private String name;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
