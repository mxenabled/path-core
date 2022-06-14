package com.mx.models.managed_cards;

import com.mx.models.MdxBase;

public final class Destination extends MdxBase<Destination> {

  private String id;

  public Destination() {

  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }
}
