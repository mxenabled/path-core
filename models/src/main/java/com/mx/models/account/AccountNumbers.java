package com.mx.models.account;

import com.mx.models.MdxBase;

public final class AccountNumbers extends MdxBase<AccountNumbers> {
  private String id;
  private AccountNumber accountNumbers;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AccountNumber getAccountNumbers() {
    return accountNumbers;
  }

  public void setAccountNumbers(AccountNumber accountNumbers) {
    this.accountNumbers = accountNumbers;
  }
}
