package com.mx.models.account;

import com.mx.models.MdxBase;

public final class AccountOwner extends MdxBase<AccountOwner> {
  private String id;
  private AccountOwnerDetails accountOwner;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AccountOwnerDetails getAccountOwner() {
    return accountOwner;
  }

  public void setAccountOwner(AccountOwnerDetails accountOwner) {
    this.accountOwner = accountOwner;
  }
}
