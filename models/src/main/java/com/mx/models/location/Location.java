package com.mx.models.location;

import java.time.LocalDate;

import com.mx.models.MdxBase;

public final class Location extends MdxBase<Location> {

  private String address;
  private String city;
  private String country;
  private Long createdAt;
  private LocalDate createdOn;
  private String details;
  private Boolean hasAtm;
  private Boolean hasServiceFee;
  private String hours;
  private String id;
  private Double latitude;
  private String locationType;
  private Double longitude;
  private String name;
  private String phoneNumber;
  private String postalCode;
  private String state;
  private Long updatedAt;
  private LocalDate updatedOn;

  public Location() {

  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String newAddress) {
    this.address = newAddress;
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

  public void setCountry(String newCountry) {
    this.country = newCountry;
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

  public String getDetails() {
    return details;
  }

  public void setDetails(String newDetails) {
    this.details = newDetails;
  }

  public Boolean getHasAtm() {
    return hasAtm;
  }

  public void setHasAtm(Boolean newHasAtm) {
    this.hasAtm = newHasAtm;
  }

  public Boolean getHasServiceFee() {
    return hasServiceFee;
  }

  public void setHasServiceFee(Boolean newHasServiceFee) {
    this.hasServiceFee = newHasServiceFee;
  }

  public String getHours() {
    return hours;
  }

  public void setHours(String newHours) {
    this.hours = newHours;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double newLatitude) {
    this.latitude = newLatitude;
  }

  public String getLocationType() {
    return locationType;
  }

  public void setLocationType(String newLocationType) {
    this.locationType = newLocationType;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double newLongitude) {
    this.longitude = newLongitude;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
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
