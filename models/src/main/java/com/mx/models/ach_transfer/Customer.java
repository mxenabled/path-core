package com.mx.models.ach_transfer;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

@Deprecated // This is going to be removed in favor of the new ACH transfer spec: https://developer.mx.com/drafts/mdx/ach_transfer/#mdx-ach-transfer
public final class Customer extends MdxBase<Customer> {

  private String id;
  private String status;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  private String email;
  @SerializedName("ip_address")
  private String ipAddress;
  @SerializedName("born_on")
  private LocalDate bornOn;
  @SerializedName("phone_number")
  private String phoneNumber;
  @SerializedName("ssn")
  private String socialSecurityNumber;
  @SerializedName("address_line_one")
  private String addressLineOne;
  @SerializedName("address_line_two")
  private String addressLineTwo;
  private String city;
  private String state;
  private String postalCode;
  @SerializedName("created_at")
  private Long createdAt;

  public Customer() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setBornOn(LocalDate bornOn) {
    this.bornOn = bornOn;
  }

  public LocalDate getBornOn() {
    return bornOn;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setAddressLineOne(String addressLineOne) {
    this.addressLineOne = addressLineOne;
  }

  public String getAddressLineOne() {
    return addressLineOne;
  }

  public void setAddressLineTwo(String addressLineTwo) {
    this.addressLineTwo = addressLineTwo;
  }

  public String getAddressLineTwo() {
    return addressLineTwo;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Long getCreatedAt() {
    return createdAt;
  }
}
