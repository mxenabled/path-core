package com.mx.models.account;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

/**
 * Represents an MDX transaction. XmlElements assigned so that it can be demarshalled from MDXv5 XML.
 */
public class Transaction extends MdxBase<Transaction> {

  @XmlElement(name = "account_guid")
  private String accountGuid;
  @XmlElement(name = "account_id")
  private String accountId;
  @XmlElement(name = "amount")
  private Double amount;
  @XmlElement(name = "category")
  private String category;
  @XmlElement(name = "category_guid")
  private String categoryGuid;
  @XmlElement(name = "check_number")
  private String checkNumber;
  @XmlElement(name = "currency_code")
  private String currencyCode;
  @XmlElement(name = "description")
  private String description;
  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "image")
  private String image;
  @XmlElement(name = "is_international")
  private Boolean isInternational;
  @XmlElement(name = "guid")
  private String guid;
  @XmlElement(name = "member_guid")
  private String memberGuid;
  @XmlElement(name = "member_id")
  private String memberId;
  @XmlElement(name = "memo")
  private String memo;
  @XmlElement(name = "metadata")
  private String metadata;
  @XmlElement(name = "latitude")
  private Double latitude;
  @XmlElement(name = "longitude")
  private Double longitude;
  @XmlElement(name = "posted_at")
  private Long postedAt;
  @XmlElement(name = "posted_on")
  private LocalDate postedOn;
  @XmlElement(name = "status")
  private String status;
  @XmlElement(name = "transacted_at")
  private Long transactedAt;
  @XmlElement(name = "transacted_on")
  private LocalDate transactedOn;
  @XmlElement(name = "type")
  private String type;
  @XmlElement(name = "user_guid")
  private String userGuid;

  public Transaction() {
    UserIdProvider.setUserId(this);
  }

  public final String getAccountGuid() {
    return accountGuid;
  }

  public final void setAccountGuid(String newAccountGuid) {
    this.accountGuid = newAccountGuid;
  }

  public final String getAccountId() {
    return accountId;
  }

  public final void setAccountId(String newAccountId) {
    this.accountId = newAccountId;
  }

  public final Double getAmount() {
    return amount;
  }

  public final void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public final String getCategory() {
    return category;
  }

  public final void setCategory(String newCategory) {
    this.category = newCategory;
  }

  public final String getCategoryGuid() {
    return categoryGuid;
  }

  public final void setCategoryGuid(String newCategoryGuid) {
    this.categoryGuid = newCategoryGuid;
  }

  public final String getCheckNumber() {
    return checkNumber;
  }

  public final void setCheckNumber(String newCheckNumber) {
    this.checkNumber = newCheckNumber;
  }

  public final String getCurrencyCode() {
    return currencyCode;
  }

  public final void setCurrencyCode(String newCurrencyCode) {
    this.currencyCode = newCurrencyCode;
  }

  public final String getDescription() {
    return description;
  }

  public final void setDescription(String newDescription) {
    this.description = newDescription;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String newId) {
    this.id = newId;
  }

  public final String getImage() {
    return image;
  }

  public final void setImage(String newImage) {
    this.image = newImage;
  }

  public final Boolean isInternational() {
    return isInternational;
  }

  public final void setInternational(Boolean newIsInternational) {
    this.isInternational = newIsInternational;
  }

  public final String getGuid() {
    return guid;
  }

  public final void setGuid(String newGuid) {
    this.guid = newGuid;
  }

  public final String getMemberGuid() {
    return memberGuid;
  }

  public final void setMemberGuid(String newMemberGuid) {
    this.memberGuid = newMemberGuid;
  }

  public final String getMemberId() {
    return memberId;
  }

  public final void setMemberId(String newMemberId) {
    this.memberId = newMemberId;
  }

  public final String getMemo() {
    return memo;
  }

  public final void setMemo(String newMemo) {
    this.memo = newMemo;
  }

  public final String getMetadata() {
    return metadata;
  }

  public final void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  public final Double getLatitude() {
    return latitude;
  }

  public final void setLatitude(Double newLatitude) {
    this.latitude = newLatitude;
  }

  public final Double getLongitude() {
    return longitude;
  }

  public final void setLongitude(Double newLongitude) {
    this.longitude = newLongitude;
  }

  public final Long getPostedAt() {
    return postedAt;
  }

  public final void setPostedAt(Long newPostedAt) {
    this.postedAt = newPostedAt;
  }

  public final LocalDate getPostedOn() {
    return postedOn;
  }

  public final void setPostedOn(LocalDate newPostedOn) {
    this.postedOn = newPostedOn;
  }

  public final String getStatus() {
    return status;
  }

  public final void setStatus(String newStatus) {
    this.status = newStatus;
  }

  public final Long getTransactedAt() {
    return transactedAt;
  }

  public final void setTransactedAt(Long newTransactedAt) {
    this.transactedAt = newTransactedAt;
  }

  public final LocalDate getTransactedOn() {
    return transactedOn;
  }

  public final void setTransactedOn(LocalDate newTransactedOn) {
    this.transactedOn = newTransactedOn;
  }

  public final String getType() {
    return type;
  }

  public final void setType(String newType) {
    this.type = newType;
  }

  public final String getUserGuid() {
    return userGuid;
  }

  public final void setUserGuid(String newUserGuid) {
    this.userGuid = newUserGuid;
  }
}
