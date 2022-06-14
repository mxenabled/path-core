package com.mx.models.payment;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class Bill extends MdxBase<Bill> {

  private Double amount;
  private Double balance;
  private String id;
  private Double minimumPayment;
  private String payeeId;
  private LocalDate paymentDueOn;
  private String status;

  public Bill() {
    UserIdProvider.setUserId(this);
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double newBalance) {
    this.balance = newBalance;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public Double getMinimumPayment() {
    return minimumPayment;
  }

  public void setMinimumPayment(Double newMinimumPayment) {
    this.minimumPayment = newMinimumPayment;
  }

  public String getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(String newPayeeId) {
    this.payeeId = newPayeeId;
  }

  public LocalDate getPaymentDueOn() {
    return paymentDueOn;
  }

  public void setPaymentDueOn(LocalDate newPaymentDueOn) {
    this.paymentDueOn = newPaymentDueOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }
}
