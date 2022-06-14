package com.mx.models.payment;

import com.mx.models.MdxBase;

public final class Merchant extends MdxBase<Merchant> {

  private String id;
  private String logoUrl;
  private String name;
  private Boolean postalCodeRequired;

  public Merchant() {

  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String newLogoUrl) {
    this.logoUrl = newLogoUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public Boolean getPostalCodeRequired() {
    return postalCodeRequired;
  }

  public void setPostalCodeRequired(Boolean postalCodeRequired) {
    this.postalCodeRequired = postalCodeRequired;
  }
}
