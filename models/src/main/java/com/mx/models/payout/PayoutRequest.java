package com.mx.models.payout;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class PayoutRequest extends MdxBase<PayoutRequest> {
  private String accountId;
  private Double amount;
  private String declineReason;
  private String id;
  private String memo;
  private String payoutMethodId;
  private String payoutId;
  private String recipientId;
  private String status;
  private LocalDate sentOn;
  private String type;

  public PayoutRequest() {
    UserIdProvider.setUserId(this);
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String newAccountId) {
    this.accountId = newAccountId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public String getDeclineReason() {
    return declineReason;
  }

  public void setDeclineReason(String newDeclineReason) {
    this.declineReason = newDeclineReason;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String newMemo) {
    this.memo = newMemo;
  }

  public String getPayoutId() {
    return payoutId;
  }

  public void setPayoutId(String newPayoutId) {
    this.payoutId = newPayoutId;
  }

  public String getPayoutMethodId() {
    return payoutMethodId;
  }

  public void setPayoutMethodId(String newPayoutMethodId) {
    this.payoutMethodId = newPayoutMethodId;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String newRecipientId) {
    this.recipientId = newRecipientId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }

  public LocalDate getSentOn() {
    return sentOn;
  }

  public void setSentOn(LocalDate newSentOn) {
    this.sentOn = newSentOn;
  }

  public String getType() {
    return type;
  }

  public void setType(String newType) {
    this.type = newType;
  }
}
