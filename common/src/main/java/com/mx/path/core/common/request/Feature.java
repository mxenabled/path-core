package com.mx.path.core.common.request;

/**
 * Enum for feature names.
 */
public enum Feature {
  /**
   * Accounts.
   */
  ACCOUNTS("accounts"),
  /**
   * ACH transfers.
   */
  ACH_TRANSFERS("ach_transfers"),
  /**
   * Authorizations.
   */
  AUTHORIZATIONS("authorizations"),
  /**
   * Check images.
   */
  CHECK_IMAGES("check_images"),
  /**
   * Credit reports.
   */
  CREDIT_REPORTS("credit_reports"),
  /**
   * Cross account transfers.
   */
  CROSS_ACCOUNT_TRANSFERS("cross_account_transfers"),

  /**
   * Documents.
   */
  DOCUMENTS("documents"),
  /**
   * Identity.
   */
  IDENTITY("identity"),

  /**
   * Location.
   */
  LOCATION("location"),
  /**
   * Managed cards.
   */
  MANAGED_CARDS("managed_cards"),
  /**
   * Originations.
   */
  ORIGINATIONS("originations"),
  /**
   * Payments.
   */
  PAYMENTS("payments"),
  /**
   * Payouts.
   */
  PAYOUTS("payouts"),
  /**
   * Products.
   */
  PRODUCTS("products"),
  /**
   * Profiles.
   */
  PROFILES("profiles"),
  /**
   * Remote deposits.
   */
  REMOTE_DEPOSITS("remote_deposits"),
  /**
   * Status.
   */
  STATUS("status"),
  /**
   * Transactions.
   */
  TRANSACTIONS("transactions"),
  /**
   * Transfers.
   */
  TRANSFERS("transfers");

  private final String name;

  /**
   * Build new {@link Feature} with specified name.
   *
   * @param name name
   */
  Feature(String name) {
    this.name = name;
  }

  /**
   * Check enum name.
   *
   * @param otherName name to check
   * @return true if name equal
   */
  public boolean equalsName(String otherName) {
    return name.equals(otherName);
  }

  /**
   * Convert name to string.
   *
   * @return name
   */
  @Override
  public String toString() {
    return this.name;
  }
}
