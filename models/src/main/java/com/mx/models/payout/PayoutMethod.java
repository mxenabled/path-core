package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class PayoutMethod extends MdxBase<PayoutMethod> {

  private String accountNumber;
  private String accountType;
  private String firstUseWarningStatus;
  private String id;
  private String isNew;
  private String nameCheckWarningStatus;
  private String payoutMethodType;
  private String recipientId;
  private String routingNumber;
  private String sendTo;
  private String status;

  public PayoutMethod() {
    UserIdProvider.setUserId(this);
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String newAccountNumber) {
    this.accountNumber = newAccountNumber;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String newAccountType) {
    this.accountType = newAccountType;
  }

  public String getFirstUseWarningStatus() {
    return firstUseWarningStatus;
  }

  public void setFirstUseWarningStatus(String newFirstUseWarningStatus) {
    this.firstUseWarningStatus = newFirstUseWarningStatus;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getIsNew() {
    return isNew;
  }

  public void setIsNew(String newIsNew) {
    this.isNew = newIsNew;
  }

  public String getNameCheckWarningStatus() {
    return nameCheckWarningStatus;
  }

  public void setNameCheckWarningStatus(String newNameCheckWarningStatus) {
    this.nameCheckWarningStatus = newNameCheckWarningStatus;
  }

  public String getPayoutMethodType() {
    return payoutMethodType;
  }

  public void setPayoutMethodType(String newPayoutMethodType) {
    this.payoutMethodType = newPayoutMethodType;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String newRecipientId) {
    this.recipientId = newRecipientId;
  }

  public String getRoutingNumber() {
    return routingNumber;
  }

  public void setRoutingNumber(String newRoutingNumber) {
    this.routingNumber = newRoutingNumber;
  }

  public String getSendTo() {
    return sendTo;
  }

  public void setSendTo(String newSendTo) {
    this.sendTo = newSendTo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }
}
