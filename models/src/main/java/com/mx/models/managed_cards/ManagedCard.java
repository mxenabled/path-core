package com.mx.models.managed_cards;

import lombok.EqualsAndHashCode;

import com.google.gson.annotations.SerializedName;
import com.mx.models.Internal;
import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;
import com.mx.models.challenges.Challenge;

@EqualsAndHashCode
public final class ManagedCard extends MdxBase<ManagedCard> {

  private String accountId;
  private String accountType;
  private String expirationOnCard;
  private String id;
  private String imageUrl;
  private String issuanceType;
  private String maskedNumberOnCard;
  private String nameOnCard;
  private String pin;
  private String newPin;
  private String status;
  private String statusMessage;
  private String type;
  private String unmaskedNumberOnCard;
  @SerializedName("cvv")
  private String unmaskedCvv;
  private Challenge[] challenges;

  // --------------------------------------------------------
  // Internal Fields
  //  ** These fields will not render in web responses.
  //  ** They are only for internal communication.
  // --------------------------------------------------------
  @Internal
  @SerializedName("card_number")
  private String cardNumber;
  @Internal
  @SerializedName("ondot_ref_id")
  private String ondotRefId;

  public ManagedCard() {
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

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getExpirationOnCard() {
    return expirationOnCard;
  }

  public void setExpirationOnCard(String newExpirationOnCard) {
    this.expirationOnCard = newExpirationOnCard;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String newImageUrl) {
    this.imageUrl = newImageUrl;
  }

  public String getIssuanceType() {
    return issuanceType;
  }

  public void setIssuanceType(String newIssuanceType) {
    this.issuanceType = newIssuanceType;
  }

  public String getMaskedNumberOnCard() {
    return maskedNumberOnCard;
  }

  public void setMaskedNumberOnCard(String newMaskedNumberOnCard) {
    this.maskedNumberOnCard = newMaskedNumberOnCard;
  }

  public String getNameOnCard() {
    return nameOnCard;
  }

  public void setNameOnCard(String newNameOnCard) {
    this.nameOnCard = newNameOnCard;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getNewPin() {
    return newPin;
  }

  public void setNewPin(String newPin) {
    this.newPin = newPin;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public String getType() {
    return type;
  }

  public void setType(String newType) {
    this.type = newType;
  }

  public String getUnmaskedNumberOnCard() {
    return unmaskedNumberOnCard;
  }

  public void setUnmaskedNumberOnCard(String unmaskedNumberOnCard) {
    this.unmaskedNumberOnCard = unmaskedNumberOnCard;
  }

  public String getUnmaskedCvv() {
    return unmaskedCvv;
  }

  public void setUnmaskedCvv(String unmaskedCvv) {
    this.unmaskedCvv = unmaskedCvv;
  }

  public Challenge[] getChallenges() {
    return challenges;
  }

  public void setChallenges(Challenge[] challenges) {
    this.challenges = challenges;
  }

  // Internal Fields

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getOndotRefId() {
    return ondotRefId;
  }

  public void setOndotRefId(String ondotRefId) {
    this.ondotRefId = ondotRefId;
  }
}
