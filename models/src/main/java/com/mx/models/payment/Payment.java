package com.mx.models.payment;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class Payment extends MdxBase<Payment> {

  private String accountId;
  private String accountType;
  private String recurringPaymentId;
  private Double amount;
  private String billId;
  private String confirmationId;
  private LocalDate deliverOn;
  private String id;
  private String memo;
  private String payeeId;
  private String payeeName;
  private String routingTransitNumber;
  private Long sentAt;
  private String sentOn;
  private String status;

  public Payment() {
    UserIdProvider.setUserId(this);
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String newAccountId) {
    this.accountId = newAccountId;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String newAccountType) {
    accountType = newAccountType;
  }

  public String getRecurringPaymentId() {
    return recurringPaymentId;
  }

  public void setRecurringPaymentId(String newRecurringPaymentId) {
    recurringPaymentId = newRecurringPaymentId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public String getBillId() {
    return billId;
  }

  public void setBillId(String newBillId) {
    this.billId = newBillId;
  }

  public LocalDate getDeliverOn() {
    return deliverOn;
  }

  public void setConfirmationId(String newConfirmationId) {
    this.confirmationId = newConfirmationId;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setDeliverOn(LocalDate newDeliverOn) {
    this.deliverOn = newDeliverOn;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    id = newId;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String newMemo) {
    this.memo = newMemo;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public void setPayeeName(String newPayeeName) {
    payeeName = newPayeeName;
  }

  public String getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(String newPayeeId) {
    this.payeeId = newPayeeId;
  }

  public String getRoutingTransitNumber() {
    return routingTransitNumber;
  }

  public void setRoutingTransitNumber(String newRoutingTransitNumber) {
    routingTransitNumber = newRoutingTransitNumber;
  }

  public Long getSentAt() {
    return sentAt;
  }

  public void setSentAt(Long newSentAt) {
    sentAt = newSentAt;
  }

  public String getSentOn() {
    return sentOn;
  }

  public void setSentOn(String newSentOn) {
    sentOn = newSentOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    status = newStatus;
  }
}
