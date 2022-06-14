package com.mx.models.cross_account_transfer;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class CrossAccountTransfer extends MdxBase<CrossAccountTransfer> {
  @Deprecated
  private String accountHolder;
  @Deprecated
  private String accountType;
  private String accountTypeId;
  private Integer accountTypeNumber;
  private Double amount;
  private String confirmationId;
  private String destinationId;
  private String fromAccountId;
  private String id;
  private String memo;
  private LocalDate postOn;
  private LocalDate postedOn;
  private String recurringCrossAccountTransferId;
  private String status;
  @Deprecated
  private String toAccountNumber;

  public CrossAccountTransfer() {
    UserIdProvider.setUserId(this);
  }

  // getter & setters

  public String getAccountHolder() {
    return accountHolder;
  }

  public void setAccountHolder(String accountHolder) {
    this.accountHolder = accountHolder;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getAccountTypeId() {
    return accountTypeId;
  }

  public void setAccountTypeId(String accountTypeId) {
    this.accountTypeId = accountTypeId;
  }

  public Integer getAccountTypeNumber() {
    return accountTypeNumber;
  }

  public void setAccountTypeNumber(Integer accountTypeNumber) {
    this.accountTypeNumber = accountTypeNumber;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String confirmationId) {
    this.confirmationId = confirmationId;
  }

  public String getDestinationId() {
    return destinationId;
  }

  public void setDestinationId(String destinationId) {
    this.destinationId = destinationId;
  }

  public String getFromAccountId() {
    return fromAccountId;
  }

  public void setFromAccountId(String fromAccountId) {
    this.fromAccountId = fromAccountId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public LocalDate getPostOn() {
    return postOn;
  }

  public void setPostOn(LocalDate postOn) {
    this.postOn = postOn;
  }

  public LocalDate getPostedOn() {
    return postedOn;
  }

  public void setPostedOn(LocalDate postedOn) {
    this.postedOn = postedOn;
  }

  public String getRecurringCrossAccountTransferId() {
    return recurringCrossAccountTransferId;
  }

  public void setRecurringCrossAccountTransferId(String recurringCrossAccountTransferId) {
    this.recurringCrossAccountTransferId = recurringCrossAccountTransferId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToAccountNumber() {
    return toAccountNumber;
  }

  public void setToAccountNumber(String toAccountNumber) {
    this.toAccountNumber = toAccountNumber;
  }
}
