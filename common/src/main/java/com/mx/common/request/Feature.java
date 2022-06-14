package com.mx.common.request;

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
  MDX_ON_DEMAND_ACCOUNTS("mdx_on_demand_accounts"),
  MDX_ON_DEMAND_SESSIONS("mdx_on_demand_sessions"),
  MDX_ON_DEMAND_TRANSACTIONS("mdx_on_demand_transactions"),
  ORIGINATIONS("originations"),
  PAYMENTS("payments"),
  PAYOUTS("payouts"),
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
