package com.mx.path.core.common.request;

public enum Feature {
  ACCOUNTS("accounts"),
  ACH_TRANSFERS("ach_transfers"),
  AUTHORIZATIONS("authorizations"),
  CHECK_IMAGES("check_images"),
  CREDIT_REPORTS("credit_reports"),
  CROSS_ACCOUNT_TRANSFERS("cross_account_transfers"),
  DOCUMENTS("documents"),
  IDENTITY("identity"),
  LOCATION("location"),
  MANAGED_CARDS("managed_cards"),
  ORIGINATIONS("originations"),
  PAYMENTS("payments"),
  PAYOUTS("payouts"),
  PRODUCTS("products"),
  PROFILES("profiles"),
  REMOTE_DEPOSITS("remote_deposits"),
  STATUS("status"),
  TRANSACTIONS("transactions"),
  TRANSFERS("transfers");

  private final String name;

  Feature(String name) {
    this.name = name;
  }

  public boolean equalsName(String otherName) {
    return name.equals(otherName);
  }

  @Override
  public String toString() {
    return this.name;
  }
}
