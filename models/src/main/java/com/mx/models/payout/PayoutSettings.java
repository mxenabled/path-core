package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

public final class PayoutSettings extends MdxBase<PayoutSettings> {

  private String id;
  private String emailAddress;
  private String payoutContactMethodType;
  private String status;

  private PayoutSettings() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String newEmailAddress) {
    this.emailAddress = newEmailAddress;
  }

  public String getPayoutContactMethodType() {
    return payoutContactMethodType;
  }

  public void setPayoutContactMethodType(String newPayoutContactMethodType) {
    this.payoutContactMethodType = newPayoutContactMethodType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String newStatus) {
    this.status = newStatus;
  }
}
