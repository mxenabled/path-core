package com.mx.models;

public class AccountType extends MdxBase<AccountType> {
  private String id;
  private String name;
  private Boolean isNumberRequired;

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

  public final Boolean getIsNumberRequired() {
    return isNumberRequired;
  }

  public final void setIsNumberRequired(Boolean numberRequired) {
    isNumberRequired = numberRequired;
  }
}
