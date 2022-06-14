package com.mx.models.profile;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;
import com.mx.models.challenges.Challenge;

public final class Phone extends MdxBase<Phone> {
  private String id;
  private List<Challenge> challenges;
  @SerializedName("phone_number")
  private String phoneNumber;
  @SerializedName("phone_type")
  private String phoneType;
  @SerializedName("work_extension")
  private String workExtension;

  public Phone() {
  }

  public List<Challenge> getChallenges() {
    return challenges;
  }

  public void setChallenges(List<Challenge> challenges) {
    this.challenges = challenges;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneType() {
    return phoneType;
  }

  public void setPhoneType(String phoneType) {
    this.phoneType = phoneType;
  }

  public String getWorkExtension() {
    return workExtension;
  }

  public void setWorkExtension(String workExtension) {
    this.workExtension = workExtension;
  }

  public enum PhoneType {
    MOBILE, HOME, WORK, FOREIGN
  }
}
