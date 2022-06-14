package com.mx.models.cross_account_transfer;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public class CrossAccountRecurringTransfer extends MdxBase<CrossAccountRecurringTransfer> {
  private String accountTypeId;
  private Integer accountTypeNumber;
  private Double amount;
  private String confirmationId;
  private String destinationId;
  private Integer endAfterAmount;
  private Integer endAfterCount;
  private LocalDate endOn;
  private String fromAccountId;
  private String frequencyId;
  private String id;
  private LocalDate lastTransferOn;
  private String memo;
  private LocalDate nextTransferOn;
  private String status;
  private LocalDate startOn;

  public CrossAccountRecurringTransfer() {
    UserIdProvider.setUserId(this);
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

  public final Double getAmount() {
    return amount;
  }

  public final void setAmount(Double amount) {
    this.amount = amount;
  }

  public final String getConfirmationId() {
    return confirmationId;
  }

  public final void setConfirmationId(String confirmationId) {
    this.confirmationId = confirmationId;
  }

  public final String getDestinationId() {
    return destinationId;
  }

  public final void setDestinationId(String destinationId) {
    this.destinationId = destinationId;
  }

  public final Integer getEndAfterAmount() {
    return endAfterAmount;
  }

  public final void setEndAfterAmount(Integer endAfterAmount) {
    this.endAfterAmount = endAfterAmount;
  }

  public final Integer getEndAfterCount() {
    return endAfterCount;
  }

  public final void setEndAfterCount(Integer endAfterCount) {
    this.endAfterCount = endAfterCount;
  }

  public final LocalDate getEndOn() {
    return endOn;
  }

  public final void setEndOn(LocalDate endOn) {
    this.endOn = endOn;
  }

  public final String getFromAccountId() {
    return fromAccountId;
  }

  public final void setFromAccountId(String fromAccountId) {
    this.fromAccountId = fromAccountId;
  }

  public final String getFrequencyId() {
    return frequencyId;
  }

  public final void setFrequencyId(String frequencyId) {
    this.frequencyId = frequencyId;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String id) {
    this.id = id;
  }

  public final LocalDate getLastTransferOn() {
    return lastTransferOn;
  }

  public final void setLastTransferOn(LocalDate lastTransferOn) {
    this.lastTransferOn = lastTransferOn;
  }

  public final String getMemo() {
    return memo;
  }

  public final void setMemo(String memo) {
    this.memo = memo;
  }

  public final LocalDate getNextTransferOn() {
    return nextTransferOn;
  }

  public final void setNextTransferOn(LocalDate nextTransferOn) {
    this.nextTransferOn = nextTransferOn;
  }

  public final String getStatus() {
    return status;
  }

  public final void setStatus(String status) {
    this.status = status;
  }

  public final LocalDate getStartOn() {
    return startOn;
  }

  public final void setStartOn(LocalDate startOn) {
    this.startOn = startOn;
  }
}
