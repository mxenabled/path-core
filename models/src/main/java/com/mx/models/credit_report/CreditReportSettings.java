package com.mx.models.credit_report;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;
import com.mx.models.MdxBase;

public final class CreditReportSettings extends MdxBase<CreditReportSettings> {
  @SerializedName("expected_schedule")
  private ExpectedSchedules expectedSchedule;
  @SerializedName("last_full_credit_report_on")
  private LocalDate lastFullCreditReportOn;
  @SerializedName("next_update_on")
  private LocalDate nextUpdateOn;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("eligibility_status")
  private EligibilityStatus eligibilityStatus;
  @SerializedName("subscription_status")
  private SubscriptionStatus subscriptionStatus;

  public CreditReportSettings() {
  }

  public ExpectedSchedules getExpectedSchedule() {
    return expectedSchedule;
  }

  public void setExpectedSchedule(ExpectedSchedules expectedSchedule) {
    this.expectedSchedule = expectedSchedule;
  }

  public LocalDate getLastFullCreditReportOn() {
    return lastFullCreditReportOn;
  }

  public void setLastFullCreditReportOn(LocalDate newLastFullCreditReportOn) {
    this.lastFullCreditReportOn = newLastFullCreditReportOn;
  }

  public LocalDate getNextUpdateOn() {
    return nextUpdateOn;
  }

  public void setNextUpdateOn(LocalDate nextUpdateOn) {
    this.nextUpdateOn = nextUpdateOn;
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

  public EligibilityStatus getEligibilityStatus() {
    return eligibilityStatus;
  }

  public void setEligibilityStatus(EligibilityStatus eligibilityStatus) {
    this.eligibilityStatus = eligibilityStatus;
  }

  public SubscriptionStatus getSubscriptionStatus() {
    return subscriptionStatus;
  }

  public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
    this.subscriptionStatus = subscriptionStatus;
  }

  // enum
  public enum ExpectedSchedules {
    MONTHLY, QUARTERLY, NO_EXPECTED_SCHEDULE
  }

  public enum EligibilityStatus {
    ELIGIBLE, INELIGIBLE
  }

  public enum SubscriptionStatus {
    SUBSCRIBED, UNSUBSCRIBED
  }
}
