package com.mx.models.payment;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class RecurringPayment extends MdxBase<RecurringPayment> {
  private String id;
  private String confirmationId;
  private String accountId;
  private String payeeId;
  private String payeeName;
  private String billId;
  private String frequencyId;
  private String status;
  private Double amount;
  private String memo;
  private LocalDate startOn;
  private Long endAfterCount;
  private Long paymentsCount;
  private LocalDate nextPaymentOn;
  private LocalDate lastPaymentOn;

  public RecurringPayment() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    id = newId;
  }

  public void setConfirmationId(String newConfirmationId) {
    this.confirmationId = newConfirmationId;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String newAccountId) {
    this.accountId = newAccountId;
  }

  public String getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(String newPayeeId) {
    this.payeeId = newPayeeId;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public void setPayeeName(String newPayeeName) {
    payeeName = newPayeeName;
  }

  public String getBillId() {
    return billId;
  }

  public void setBillId(String newBillId) {
    this.billId = newBillId;
  }

  public String getFrequencyId() {
    return frequencyId;
  }

  public void setFrequencyId(String newFrequencyId) {
    this.frequencyId = newFrequencyId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    status = newStatus;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String newMemo) {
    this.memo = newMemo;
  }

  public Long getEndAfterCount() {
    return endAfterCount;
  }

  public void setEndAfterCount(Long newEndAfterCount) {
    endAfterCount = newEndAfterCount;
  }

  public LocalDate getStartOn() {
    return startOn;
  }

  public void setStartOn(LocalDate newStartOn) {
    startOn = newStartOn;
  }

  public Long getPaymentsCount() {
    return paymentsCount;
  }

  public void setPaymentsCount(Long newPaymentsCount) {
    paymentsCount = newPaymentsCount;
  }

  public LocalDate getNextPaymentOn() {
    return nextPaymentOn;
  }

  public void setNextPaymentOn(LocalDate newNextPaymentOn) {
    nextPaymentOn = newNextPaymentOn;
  }

  public LocalDate getLastPaymentOn() {
    return lastPaymentOn;
  }

  public void setLastPaymentOn(LocalDate newLastPaymentOn) {
    lastPaymentOn = newLastPaymentOn;
  }
}
