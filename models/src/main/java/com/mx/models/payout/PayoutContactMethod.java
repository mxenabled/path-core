package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class PayoutContactMethod extends MdxBase<PayoutContactMethod> {

  private String emailAddress;
  private String id;
  private String payoutContactMethodType;
  private String phoneNumber;
  private String status;

  public PayoutContactMethod() {
    UserIdProvider.setUserId(this);
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String newEmailAddress) {
    this.emailAddress = newEmailAddress;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getPayoutContactMethodType() {
    return payoutContactMethodType;
  }

  public void setPayoutContactMethodType(String newPayoutContactMethodType) {
    this.payoutContactMethodType = newPayoutContactMethodType;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String newPhoneNumber) {
    this.phoneNumber = newPhoneNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }
}
