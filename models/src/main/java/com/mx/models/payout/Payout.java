package com.mx.models.payout;

import java.time.LocalDate;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class Payout extends MdxBase<Payout> {

  private String accountId;
  private Double amount;
  private String confirmationId;
  private LocalDate expiresOn;
  private Double fee;
  private String id;
  private String memo;
  private String nameCheckWarningTitle;
  private String nameCheckWarningText;
  private String payoutMethodId;
  private String payoutRequestId;
  private String recipientId;
  private String recurringPayoutId;
  private String status;
  private String senderName;
  private LocalDate sendOn;
  private Long sentAt;
  private LocalDate sentOn;
  private String speedText;
  private String token;
  private String type;
  private String challengeQuestion;
  private String challengeAnswer;

  public Payout() {
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

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String newConfirmationId) {
    this.confirmationId = newConfirmationId;
  }

  public LocalDate getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(LocalDate newExpiresOn) {
    this.expiresOn = newExpiresOn;
  }

  public Double getFee() {
    return fee;
  }

  public void setFee(Double newFee) {
    this.fee = newFee;
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

  public String getNameCheckWarningTitle() {
    return nameCheckWarningTitle;
  }

  public void setNameCheckWarningTitle(String newNameCheckWarningTitle) {
    this.nameCheckWarningTitle = newNameCheckWarningTitle;
  }

  public String getNameCheckWarningText() {
    return nameCheckWarningText;
  }

  public void setNameCheckWarningText(String newNameCheckWarningText) {
    this.nameCheckWarningText = newNameCheckWarningText;
  }

  public String getPayoutMethodId() {
    return payoutMethodId;
  }

  public void setPayoutMethodId(String payoutMethodId) {
    this.payoutMethodId = payoutMethodId;
  }

  public String getPayoutRequestId() {
    return payoutRequestId;
  }

  public void setPayoutRequestId(String newPayoutrequestId) {
    this.payoutRequestId = newPayoutrequestId;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String recipientId) {
    this.recipientId = recipientId;
  }

  public String getRecurringPayoutId() {
    return recurringPayoutId;
  }

  public void setRecurringPayoutId(String newRecurringPayoutId) {
    this.recurringPayoutId = newRecurringPayoutId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String newSenderName) {
    this.senderName = newSenderName;
  }

  public LocalDate getSendOn() {
    return sendOn;
  }

  public void setSendOn(LocalDate newSentOnAt) {
    this.sendOn = newSentOnAt;
  }

  public Long getSentAt() {
    return sentAt;
  }

  public void setSentAt(Long newSentAt) {
    this.sentAt = newSentAt;
  }

  public LocalDate getSentOn() {
    return sentOn;
  }

  public void setSentOn(LocalDate newSentOn) {
    this.sentOn = newSentOn;
  }

  public String getSpeedText() {
    return speedText;
  }

  public void setSpeedText(String newSpeedText) {
    this.speedText = newSpeedText;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String newToken) {
    this.token = newToken;
  }

  public String getType() {
    return type;
  }

  public void setType(String newType) {
    this.type = newType;
  }

  public String getChallengeQuestion() {
    return challengeQuestion;
  }

  public void setChallengeQuestion(String challengeQuestion) {
    this.challengeQuestion = challengeQuestion;
  }

  public String getChallengeAnswer() {
    return challengeAnswer;
  }

  public void setChallengeAnswer(String challengeAnswer) {
    this.challengeAnswer = challengeAnswer;
  }
}
