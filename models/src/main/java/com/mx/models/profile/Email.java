package com.mx.models.profile;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class Email extends MdxBase<Email> {
  @SerializedName("email_address")
  private String emailAddress;
  @SerializedName("email_type")
  private String emailType;
  private String id;

  public Email() {
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getEmailType() {
    return emailType;
  }

  public void setEmailType(String emailType) {
    this.emailType = emailType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public enum EmailType {
    BUSINESS, PERSONAL
  }
}
