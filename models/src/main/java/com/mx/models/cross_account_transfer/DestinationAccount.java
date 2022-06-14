package com.mx.models.cross_account_transfer;

import com.mx.models.MdxBase;

public class DestinationAccount extends MdxBase<DestinationAccount> {

  private String accountHolder;
  private String accountNumber;
  private String accountTypeId;
  private Integer accountTypeNumber;
  private String id;
  private String nickname;

  public DestinationAccount() {
  }

  public final String getAccountHolder() {
    return accountHolder;
  }

  public final void setAccountHolder(String accountHolder) {
    this.accountHolder = accountHolder;
  }

  public final String getAccountNumber() {
    return accountNumber;
  }

  public final void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public final String getAccountTypeId() {
    return accountTypeId;
  }

  public final void setAccountTypeId(String accountTypeId) {
    this.accountTypeId = accountTypeId;
  }

  public final Integer getAccountTypeNumber() {
    return accountTypeNumber;
  }

  public final void setAccountTypeNumber(Integer accountTypeNumber) {
    this.accountTypeNumber = accountTypeNumber;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String id) {
    this.id = id;
  }

  public final String getNickname() {
    return nickname;
  }

  public final void setNickname(String nickname) {
    this.nickname = nickname;
  }
}
