package com.mx.models.payment;

import java.util.List;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;
import com.mx.models.challenges.Challenge;

public final class Payee extends MdxBase<Payee> {

  private String accountNumber;
  private String address;
  private String address2;
  private List<Challenge> challenges;
  private String city;
  private String country;
  private String id;
  private String logoUrl;
  private String merchantId;
  private String name;
  private String nextPaymentDeliverableOn;
  private String nickname;
  private String payeeType;
  private String phoneNumber;
  private String postalCode;
  private String state;

  public Payee() {
    UserIdProvider.setUserId(this);
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String newAccountNumber) {
    this.accountNumber = newAccountNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String newAddress) {
    this.address = newAddress;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String newAddress2) {
    this.address2 = newAddress2;
  }

  public List<Challenge> getChallenges() {
    return challenges;
  }

  public void setChallenges(List<Challenge> challenges) {
    this.challenges = challenges;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String newCity) {
    this.city = newCity;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String newLogoUrl) {
    this.logoUrl = newLogoUrl;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String newMerchantId) {
    this.merchantId = newMerchantId;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public String getNextPaymentDeliverableOn() {
    return nextPaymentDeliverableOn;
  }

  public void setNextPaymentDeliverableOn(String newNextPaymentDeliverableOn) {
    this.nextPaymentDeliverableOn = newNextPaymentDeliverableOn;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String newNickname) {
    this.nickname = newNickname;
  }

  public String getPayeeType() {
    return payeeType;
  }

  public void setPayeeType(String newPayeeType) {
    this.payeeType = newPayeeType;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String newPhoneNumber) {
    this.phoneNumber = newPhoneNumber;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String newPostalCode) {
    this.postalCode = newPostalCode;
  }

  public String getState() {
    return state;
  }

  public void setState(String newState) {
    this.state = newState;
  }
}
