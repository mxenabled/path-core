package com.mx.models.dispute;

import javax.xml.bind.annotation.XmlElement;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

/**
 * Represents an MDX dispute. XmlElements assigned so that it can be demarshalled from MDXv5 XML.
 */
public class Dispute extends MdxBase<Dispute> {

  @XmlElement(name = "account_id")
  private String accountId;
  @XmlElement(name = "amount_issued")
  private Double amountIssued;
  @XmlElement(name = "cancellation_id")
  private String cancellationId;
  @XmlElement(name = "cancelled_on")
  private String cancelledOn;
  @XmlElement(name = "card_id")
  private String cardId;
  @XmlElement(name = "case_number")
  private String caseNumber;
  @XmlElement(name = "contact_phone")
  private String contactPhone;
  @XmlElement(name = "credit_requested_on")
  private String creditRequestedOn;
  @XmlElement(name = "discovered_on")
  private String discoveredOn;
  @XmlElement(name = "dispute_type")
  private String disputeType;
  @XmlElement(name = "expected_on")
  private String expectedOn;
  @XmlElement(name = "expected_original_amount")
  private Double expectedOriginalAmount;
  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "is_card_in_possession")
  private Boolean isCardInPossession;
  @XmlElement(name = "is_credit_requested")
  private Boolean isCreditRequested;
  @XmlElement(name = "is_reported_to_police")
  private Boolean isReportedToPolice;
  @XmlElement(name = "is_sms_contact_authorized")
  private Boolean isSmsContactAuthorized;
  @XmlElement(name = "issued_on")
  private String issuedOn;
  @XmlElement(name = "member_name")
  private String memberName;
  @XmlElement(name = "merchandise_description")
  private String merchandiseDescription;
  @XmlElement(name = "merchant_contacted_on")
  private String merchantContactedOn;
  @XmlElement(name = "police_agency")
  private String policeAgency;
  @XmlElement(name = "reason")
  private String reason;
  @XmlElement(name = "reported_on")
  private String reportedOn;
  @XmlElement(name = "returned_on")
  private String returnedOn;
  @XmlElement(name = "source_image")
  private String sourceImage;
  @XmlElement(name = "status")
  private String status;

  public Dispute() {
    UserIdProvider.setUserId(this);
  }

  public final String getAccountId() {
    return accountId;
  }

  public final void setAccountId(String newAccountId) {
    this.accountId = newAccountId;
  }

  public final Double getAmountIssued() {
    return amountIssued;
  }

  public final void setAmountIssued(Double newAmountIssued) {
    this.amountIssued = newAmountIssued;
  }

  public final String getCancellationId() {
    return cancellationId;
  }

  public final void setCancellationId(String newCancellationId) {
    this.cancellationId = newCancellationId;
  }

  public final String getCancelledOn() {
    return cancelledOn;
  }

  public final void setCancelledOn(String newCancelledOn) {
    this.cancelledOn = newCancelledOn;
  }

  public final String getCardId() {
    return cardId;
  }

  public final void setCardId(String newCardId) {
    this.cardId = newCardId;
  }

  public final String getCaseNumber() {
    return caseNumber;
  }

  public final void setCaseNumber(String newCaseNumber) {
    this.caseNumber = newCaseNumber;
  }

  public final String getContactPhone() {
    return contactPhone;
  }

  public final void setContactPhone(String newContactPhone) {
    this.contactPhone = newContactPhone;
  }

  public final String getCreditRequestedOn() {
    return creditRequestedOn;
  }

  public final void setCreditRequestedOn(String newCreditRequestedOn) {
    this.creditRequestedOn = newCreditRequestedOn;
  }

  public final String getDiscoveredOn() {
    return discoveredOn;
  }

  public final void setDiscoveredOn(String newDiscoveredOn) {
    this.discoveredOn = newDiscoveredOn;
  }

  public final String getDisputeType() {
    return disputeType;
  }

  public final void setDisputeType(String newDisputeType) {
    this.disputeType = newDisputeType;
  }

  public final String getExpectedOn() {
    return expectedOn;
  }

  public final void setExpectedOn(String newExpectedOn) {
    this.expectedOn = newExpectedOn;
  }

  public final Double getExpectedOriginalAmount() {
    return expectedOriginalAmount;
  }

  public final void setExpectedOriginalAmount(Double newExpectedOriginalAmount) {
    this.expectedOriginalAmount = newExpectedOriginalAmount;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String newId) {
    this.id = newId;
  }

  public final Boolean isCardInPossession() {
    return isCardInPossession;
  }

  public final void setIsCardInPossession(Boolean newIsCardInPossession) {
    this.isCardInPossession = newIsCardInPossession;
  }

  public final Boolean isCreditRequested() {
    return isCreditRequested;
  }

  public final void setIsCreditRequested(Boolean newIsCreditRequested) {
    this.isCreditRequested = newIsCreditRequested;
  }

  public final Boolean isReportedToPolice() {
    return isReportedToPolice;
  }

  public final void setIsReportedToPolice(Boolean newIsReportedToPolice) {
    this.isReportedToPolice = newIsReportedToPolice;
  }

  public final Boolean isSmsContactAuthorized() {
    return isSmsContactAuthorized;
  }

  public final void setIsSmsContactAuthorized(Boolean newIsSmsContactAuthorized) {
    this.isSmsContactAuthorized = newIsSmsContactAuthorized;
  }

  public final String getIssuedOn() {
    return issuedOn;
  }

  public final void setIssuedOn(String newIssuedOn) {
    this.issuedOn = newIssuedOn;
  }

  public final String getMemberName() {
    return memberName;
  }

  public final void setMemberName(String newMemberName) {
    this.memberName = newMemberName;
  }

  public final String getMerchandiseDescription() {
    return merchandiseDescription;
  }

  public final void setMerchandiseDescription(String newMerchandiseDescription) {
    this.merchandiseDescription = newMerchandiseDescription;
  }

  public final String getMerchantContactedOn() {
    return merchantContactedOn;
  }

  public final void setMerchantContactedOn(String newMerchantContactedOn) {
    this.merchantContactedOn = newMerchantContactedOn;
  }

  public final String getPoliceAgency() {
    return policeAgency;
  }

  public final void setPoliceAgency(String newPoliceAgency) {
    this.policeAgency = newPoliceAgency;
  }

  public final String getReason() {
    return reason;
  }

  public final void setReason(String newReason) {
    this.reason = newReason;
  }

  public final String getReportedOn() {
    return reportedOn;
  }

  public final void setReportedOn(String newReportedOn) {
    this.reportedOn = newReportedOn;
  }

  public final String getReturnedOn() {
    return returnedOn;
  }

  public final void setReturnedOn(String newReturnedOn) {
    this.returnedOn = newReturnedOn;
  }

  public final String getSourceImage() {
    return sourceImage;
  }

  public final void setSourceImage(String newSourceImage) {
    this.sourceImage = newSourceImage;
  }

  public final String getStatus() {
    return status;
  }

  public final void setStatus(String newStatus) {
    this.status = newStatus;
  }
}
