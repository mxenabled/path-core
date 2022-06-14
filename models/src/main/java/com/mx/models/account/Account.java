package com.mx.models.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.SerializedName;
import com.mx.models.Internal;
import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

@SuppressWarnings("PMD.CyclomaticComplexity")
public class Account extends MdxBase<Account> {
  @XmlElement(name = "available_balance")
  private BigDecimal availableBalance;
  @XmlElement(name = "account_number")
  private String accountNumber;
  @XmlElement(name = "available_credit")
  private BigDecimal availableCredit;
  @XmlElement(name = "balance")
  private BigDecimal balance;
  @XmlElement(name = "apr")
  private Double apr;
  @XmlElement(name = "apy")
  private Double apy;
  @XmlElement(name = "credit_limit")
  private BigDecimal creditLimit;
  @XmlElement(name = "currency_code")
  private String currencyCode;
  @XmlElement(name = "day_payment_is_due")
  private LocalDate dayPaymentIsDue;
  @XmlElement(name = "guid")
  private String guid;
  @XmlElement(name = "has_monthly_transfer_limit")
  private Boolean hasMonthlyTransferLimit;
  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "interest_rate")
  private Double interestRate;
  @XmlElement(name = "is_closed")
  private Boolean isClosed;
  @XmlElement(name = "is_hidden")
  private Boolean isHidden;
  @XmlElement(name = "last_payment")
  private BigDecimal lastPayment;
  @XmlElement(name = "last_payment_at")
  private Long lastPaymentAt;
  @XmlElement(name = "last_payment_on")
  private LocalDate lastPaymentOn;
  @XmlElement(name = "matures_at")
  private Long maturesAt;
  @XmlElement(name = "matures_on")
  private LocalDate maturesOn;
  @XmlElement(name = "member_guid")
  private String memberGuid;
  @XmlElement(name = "member_id")
  private String memberId;
  @XmlElement(name = "metadata")
  private String metadata;
  @XmlElement(name = "minimum_balance")
  private BigDecimal minimumBalance;
  @XmlElement(name = "minimum_payment")
  private BigDecimal minimumPayment;
  @XmlElement(name = "monthly_transfer_count")
  private Integer monthlyTransferCount;
  @XmlElement(name = "name")
  private String name;
  @XmlElement(name = "nickname")
  private String nickname;
  @XmlElement(name = "original_balance")
  private BigDecimal originalBalance;
  @XmlElement(name = "past_due_amount")
  private BigDecimal pastDueAmount;
  @XmlElement(name = "past_due_on")
  private String pastDueOn;
  @XmlElement(name = "payment_due_at")
  private Long paymentDueAt;
  @XmlElement(name = "payment_due_on")
  private LocalDate paymentDueOn;
  @XmlElement(name = "payoff_balance")
  private BigDecimal payoffBalance;
  @XmlElement(name = "pending_balance")
  private BigDecimal pendingBalance;
  @XmlElement(name = "routing_number")
  private String routingNumber;
  @Deprecated
  @XmlElement(name = "routing_transit_number")
  private String routingTransitNumber;
  @XmlElement(name = "started_at")
  private Long startedAt;
  @XmlElement(name = "started_on")
  private LocalDate startedOn;
  @XmlElement(name = "statement_balance")
  private BigDecimal statementBalance;
  @XmlElement(name = "subtype")
  private String subtype;
  @XmlElement(name = "type")
  private String type;

  // --------------------------------------------------------
  // Internal Fields
  //  ** These fields will not render in web responses.
  //  ** They are only for internal communication.
  // --------------------------------------------------------
  @Internal
  @SerializedName("account_micr_number")
  private String accountMICRNumber;
  @Internal
  @SerializedName("full_account_number")
  private String fullAccountNumber;
  @Internal
  @SerializedName("member_number")
  private String memberNumber;

  // Account Capabilities

  @Internal
  @SerializedName("bill_pay_from")
  private boolean billPayFrom;
  @Internal
  @SerializedName("remote_deposit_to")
  private boolean remoteDepositTo;
  @Internal
  @SerializedName("transfer_from")
  private boolean transferFrom;
  @Internal
  @SerializedName("transfer_to")
  private boolean transferTo;

  public Account() {
    UserIdProvider.setUserId(this);
  }

  public final BigDecimal getAvailableBalance() {
    return availableBalance;
  }

  public final void setAvailableBalance(BigDecimal newAvailableBalance) {
    this.availableBalance = newAvailableBalance;
  }

  public final String getAccountNumber() {
    return accountNumber;
  }

  public final void setAccountNumber(String newAccountNumber) {
    this.accountNumber = newAccountNumber;
  }

  public final Double getApr() {
    return apr;
  }

  public final void setApr(Double newApr) {
    this.apr = newApr;
  }

  public final Double getApy() {
    return apy;
  }

  public final void setApy(Double newApy) {
    this.apy = newApy;
  }

  public final BigDecimal getAvailableCredit() {
    return availableCredit;
  }

  public final void setAvailableCredit(BigDecimal newAvailableCredit) {
    this.availableCredit = newAvailableCredit;
  }

  public final BigDecimal getBalance() {
    return balance;
  }

  public final void setBalance(BigDecimal newBalance) {
    this.balance = newBalance;
  }

  public final BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public final void setCreditLimit(BigDecimal newCreditLimit) {
    this.creditLimit = newCreditLimit;
  }

  public final String getCurrencyCode() {
    return currencyCode;
  }

  public final void setCurrencyCode(String newCurrencyCode) {
    this.currencyCode = newCurrencyCode;
  }

  public final LocalDate getDayPaymentIsDue() {
    return dayPaymentIsDue;
  }

  public final void setDayPaymentIsDue(LocalDate newDayPaymentIsDue) {
    this.dayPaymentIsDue = newDayPaymentIsDue;
  }

  public final String getGuid() {
    return guid;
  }

  public final void setGuid(String newGuid) {
    this.guid = newGuid;
  }

  public final Boolean getHasMonthlyTransferLimit() {
    return this.hasMonthlyTransferLimit;
  }

  public final void setHasMonthlyTransferLimit(Boolean hasMonthlyTransferLimit) {
    this.hasMonthlyTransferLimit = hasMonthlyTransferLimit;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String newId) {
    this.id = newId;
  }

  public final Double getInterestRate() {
    return interestRate;
  }

  public final void setInterestRate(Double newInterestRate) {
    this.interestRate = newInterestRate;
  }

  public final Boolean getIsClosed() {
    return isClosed;
  }

  public final void setIsClosed(Boolean newIsClosed) {
    this.isClosed = newIsClosed;
  }

  public final Boolean getIsHidden() {
    return isHidden;
  }

  public final void setIsHidden(Boolean newIsHidden) {
    this.isHidden = newIsHidden;
  }

  public final BigDecimal getLastPayment() {
    return lastPayment;
  }

  public final void setLastPayment(BigDecimal newLastPayment) {
    this.lastPayment = newLastPayment;
  }

  public final Long getLastPaymentAt() {
    return lastPaymentAt;
  }

  public final void setLastPaymentAt(Long newLastPaymentAt) {
    this.lastPaymentAt = newLastPaymentAt;
  }

  public final LocalDate getLastPaymentOn() {
    return lastPaymentOn;
  }

  public final void setLastPaymentOn(LocalDate newLastPaymentOn) {
    this.lastPaymentOn = newLastPaymentOn;
  }

  public final Long getMaturesAt() {
    return maturesAt;
  }

  public final void setMaturesAt(Long newMaturesAt) {
    this.maturesAt = newMaturesAt;
  }

  public final LocalDate getMaturesOn() {
    return maturesOn;
  }

  public final void setMaturesOn(LocalDate newMaturesOn) {
    this.maturesOn = newMaturesOn;
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

  public final String getMetadata() {
    return metadata;
  }

  public final void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  public final BigDecimal getMinimumBalance() {
    return minimumBalance;
  }

  public final void setMinimumBalance(BigDecimal newMinimumBalance) {
    this.minimumBalance = newMinimumBalance;
  }

  public final BigDecimal getMinimumPayment() {
    return minimumPayment;
  }

  public final void setMinimumPayment(BigDecimal newMinimumPayment) {
    this.minimumPayment = newMinimumPayment;
  }

  public final Integer getMonthlyTransferCount() {
    return this.monthlyTransferCount;
  }

  public final void setMonthlyTransferCount(Integer monthlyTransferCount) {
    this.monthlyTransferCount = monthlyTransferCount;
  }

  public final String getName() {
    return name;
  }

  public final void setName(String newName) {
    this.name = newName;
  }

  public final String getNickname() {
    return nickname;
  }

  public final void setNickname(String newNickname) {
    this.nickname = newNickname;
  }

  public final BigDecimal getOriginalBalance() {
    return originalBalance;
  }

  public final void setOriginalBalance(BigDecimal newOriginalBalance) {
    this.originalBalance = newOriginalBalance;
  }

  public final BigDecimal getPastDueAmount() {
    return pastDueAmount;
  }

  public final void setPastDueAmount(BigDecimal newPastDueAmount) {
    this.pastDueAmount = newPastDueAmount;
  }

  public final String getPastDueOn() {
    return pastDueOn;
  }

  public final void setPastDueOn(String newPastDueOn) {
    this.pastDueOn = newPastDueOn;
  }

  public final Long getPaymentDueAt() {
    return paymentDueAt;
  }

  public final void setPaymentDueAt(Long newPaymentDueAt) {
    this.paymentDueAt = newPaymentDueAt;
  }

  public final LocalDate getPaymentDueOn() {
    return paymentDueOn;
  }

  public final void setPaymentDueOn(LocalDate newPaymentDueOn) {
    this.paymentDueOn = newPaymentDueOn;
  }

  public final BigDecimal getPayoffBalance() {
    return payoffBalance;
  }

  public final void setPayoffBalance(BigDecimal newPayoffBalance) {
    this.payoffBalance = newPayoffBalance;
  }

  public final BigDecimal getPendingBalance() {
    return pendingBalance;
  }

  public final void setPendingBalance(BigDecimal pendingBalance) {
    this.pendingBalance = pendingBalance;
  }

  public final String getRoutingNumber() {
    return routingNumber;
  }

  public final void setRoutingNumber(String newRoutingNumber) {
    this.routingNumber = newRoutingNumber;
  }

  public final String getRoutingTransitNumber() {
    return routingTransitNumber;
  }

  public final void setRoutingTransitNumber(String newRoutingTransitNumber) {
    this.routingTransitNumber = newRoutingTransitNumber;
  }

  public final Long getStartedAt() {
    return startedAt;
  }

  public final void setStartedAt(Long newStartedAt) {
    this.startedAt = newStartedAt;
  }

  public final LocalDate getStartedOn() {
    return startedOn;
  }

  public final void setStartedOn(LocalDate newStartedOn) {
    this.startedOn = newStartedOn;
  }

  public final BigDecimal getStatementBalance() {
    return statementBalance;
  }

  public final void setStatementBalance(BigDecimal newStatementBalance) {
    this.statementBalance = newStatementBalance;
  }

  public final String getSubtype() {
    return subtype;
  }

  public final void setSubtype(String newSubtype) {
    this.subtype = newSubtype;
  }

  public final String getType() {
    return type;
  }

  public final void setType(String newType) {
    this.type = newType;
  }

  // Internal Fields

  public final String getAccountMICRNumber() {
    return accountMICRNumber;
  }

  public final void setAccountMICRNumber(String accountMICRNumber) {
    this.accountMICRNumber = accountMICRNumber;
  }

  public final String getFullAccountNumber() {
    return fullAccountNumber;
  }

  public final void setFullAccountNumber(String fullAccountNumber) {
    this.fullAccountNumber = fullAccountNumber;
  }

  public final String getMemberNumber() {
    return memberNumber;
  }

  public final void setMemberNumber(String memberNumber) {
    this.memberNumber = memberNumber;
  }

  public final boolean isBillPayFrom() {
    return billPayFrom;
  }

  public final void setBillPayFrom(boolean billPayFrom) {
    this.billPayFrom = billPayFrom;
  }

  public final boolean isRemoteDepositTo() {
    return remoteDepositTo;
  }

  public final void setRemoteDepositTo(boolean remoteDepositTo) {
    this.remoteDepositTo = remoteDepositTo;
  }

  public final boolean isTransferFrom() {
    return transferFrom;
  }

  public final void setTransferFrom(boolean transferFrom) {
    this.transferFrom = transferFrom;
  }

  public final boolean isTransferTo() {
    return transferTo;
  }

  public final void setTransferTo(boolean transferTo) {
    this.transferTo = transferTo;
  }
}
