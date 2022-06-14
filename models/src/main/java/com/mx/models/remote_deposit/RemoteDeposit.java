package com.mx.models.remote_deposit;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;
import com.mx.models.MdxRelationId;
import com.mx.models.UserIdProvider;
import com.mx.models.account.Account;

public final class RemoteDeposit extends MdxBase<RemoteDeposit> {
  @SerializedName("account_id")
  private String accountId;
  private Double amount;
  @SerializedName("back_of_check_image")
  private String backOfCheckImage;
  private String confirmationId;
  private Long createdAt;
  private LocalDate createdOn;
  private String failureReason;
  private String failureType;
  @SerializedName("front_of_check_image")
  private String frontOfCheckImage;
  private String id;
  private String memo;
  private Long postedAt;
  private LocalDate postedOn;
  private String status;
  private Long updatedAt;
  private LocalDate updatedOn;

  public RemoteDeposit() {
    UserIdProvider.setUserId(this);
  }

  @MdxRelationId(referredClass = Account.class)
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

  public String getBackOfCheckImage() {
    return backOfCheckImage;
  }

  public void setBackOfCheckImage(String newBackOfCheckImage) {
    this.backOfCheckImage = newBackOfCheckImage;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String newConfirmationId) {
    this.confirmationId = newConfirmationId;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long newCreatedAt) {
    this.createdAt = newCreatedAt;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate newCreatedOn) {
    this.createdOn = newCreatedOn;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String newFailureReason) {
    this.failureReason = newFailureReason;
  }

  public String getFailureType() {
    return failureType;
  }

  public void setFailureType(String newFailureType) {
    this.failureType = newFailureType;
  }

  public String getFrontOfCheckImage() {
    return frontOfCheckImage;
  }

  public void setFrontOfCheckImage(String newFrontOfCheckImage) {
    this.frontOfCheckImage = newFrontOfCheckImage;
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

  public Long getPostedAt() {
    return postedAt;
  }

  public void setPostedAt(Long newPostedAt) {
    this.postedAt = newPostedAt;
  }

  public LocalDate getPostedOn() {
    return postedOn;
  }

  public void setPostedOn(LocalDate newPostedOn) {
    this.postedOn = newPostedOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long newUpdatedAt) {
    this.updatedAt = newUpdatedAt;
  }

  public LocalDate getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(LocalDate newUpdatedOn) {
    this.updatedOn = newUpdatedOn;
  }
}
