package com.mx.models.payout;

import com.mx.models.MdxBase;
import com.mx.models.MdxList;
import com.mx.models.UserIdProvider;

public final class Recipient extends MdxBase<Recipient> {

  private String firstName;
  private String id;
  private String lastName;
  private String primaryPayoutMethodId;
  private MdxList<PayoutMethod> payoutMethods;

  public Recipient() {
    UserIdProvider.setUserId(this);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String newFirstName) {
    this.firstName = newFirstName;
  }

  public String getId() {
    return id;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String newLastName) {
    this.lastName = newLastName;
  }

  public String getPrimaryPayoutMethodId() {
    return primaryPayoutMethodId;
  }

  public void setPrimaryPayoutMethodId(String newPrimaryPayoutMethodId) {
    this.primaryPayoutMethodId = newPrimaryPayoutMethodId;
  }

  public MdxList<PayoutMethod> getPayoutMethods() {
    return payoutMethods;
  }

  public void setPayoutMethods(MdxList<PayoutMethod> payoutMethods) {
    this.payoutMethods = payoutMethods;
  }
}
