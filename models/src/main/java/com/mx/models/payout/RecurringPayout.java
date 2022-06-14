package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class RecurringPayout extends MdxBase<RecurringPayout> {

  private String accountId;
  private Double amount;
  private String challengeQuestion;
  private String challengeAnswer;
  private String confirmationId;
  private Double endAfterAmount;
  private Double endAfterCount;
  private String endOn;
  private String frequencyId;
  private String id;
  private String memo;
  private String payoutMethodId;
  private String recipientId;
  private String startOn;
  private String status;
  private String token;

  public RecurringPayout() {
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

  public void setAmount(Double amount) {
    this.amount = amount;
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

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String newConfirmationId) {
    this.confirmationId = newConfirmationId;
  }

  public Double getEndAfterAmount() {
    return endAfterAmount;
  }

  public void setEndAfterAmount(Double newEndAfterAmount) {
    this.endAfterAmount = newEndAfterAmount;
  }

  public Double getEndAfterCount() {
    return endAfterCount;
  }

  public void setEndAfterCount(Double newEndAfterCcount) {
    this.endAfterCount = newEndAfterCcount;
  }

  public String getEndOn() {
    return endOn;
  }

  public void setEndOn(String newEndOn) {
    this.endOn = newEndOn;
  }

  public String getFrequencyId() {
    return frequencyId;
  }

  public void setFrequencyId(String newFrequencyId) {
    this.frequencyId = newFrequencyId;
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

  public String getStartOn() {
    return startOn;
  }

  public void setStartOn(String newStartOn) {
    this.startOn = newStartOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String newToken) {
    this.token = newToken;
  }
}
