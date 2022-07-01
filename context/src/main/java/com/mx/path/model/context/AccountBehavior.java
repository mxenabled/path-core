package com.mx.path.model.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>Defines account behaviors and attributes.</b>
 *
 * <p>
 * Used to inform child services of account attributes, behaviors and capabilities.
 * Loaded into session by primary service (usually at authentication time).
 * These can be inspected and the behaviors can be discovered/applied
 * applied via this class.
 * </p>
 *
 * @deprecated AccountBehavior is no longer supported. Applications using the SDK should build the functionality they
 *             need from this class into the Accessor code.
 */
@Deprecated
public class AccountBehavior {

  // Fields

  private List<SessionAccountOwner> accountOwners = new ArrayList<>();
  private String accountSuffix;
  private String accountType;
  private boolean canReceiveRemoteDeposit;
  private String extAccountType;
  private Map<Session.ServiceIdentifier, String> mappedAccountIds = new HashMap<>();
  private String maskedAccountNumber;
  private String serviceAccountId;
  private boolean canTransferFrom;
  private boolean canTransferTo;

  // Getter/setters

  /**
   * @return the accountOwners
   */
  public final List<SessionAccountOwner> getAccountOwners() {
    return accountOwners;
  }

  /**
   * @param accountOwners the accountOwners to set
   */
  public final void setAccountOwners(List<SessionAccountOwner> accountOwners) {
    this.accountOwners = accountOwners;
  }

  /**
   * @return the accountSuffix
   */
  public final String getAccountSuffix() {
    return accountSuffix;
  }

  /**
   * @param accountSuffix the accountSuffix to set
   */
  public final void setAccountSuffix(String accountSuffix) {
    this.accountSuffix = accountSuffix;
  }

  /**
   * @return the accountType
   */
  public final String getAccountType() {
    return accountType;
  }

  /**
   * @param accountType the accountType to set
   */
  public final void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  /**
   * This account can participate in remote deposits
   * @return
   */
  public final boolean canReceiveRemoteDeposit() {
    return canReceiveRemoteDeposit;
  }

  /**
   * @param canReceiveRemoteDeposit
   */
  public final void setCanReceiveRemoteDeposit(boolean canReceiveRemoteDeposit) {
    this.canReceiveRemoteDeposit = canReceiveRemoteDeposit;
  }

  /**
   * External or Alternative account type
   *
   * @return the accountTypeExt
   */
  public final String getExtAccountType() {
    return extAccountType;
  }

  /**
   * @param extAccountType the accountType to set
   */
  public final void setExtAccountType(String extAccountType) {
    this.extAccountType = extAccountType;
  }

  /**
   * @return External service account id mappings
   */
  public final Map<Session.ServiceIdentifier, String> getMappedAccountIds() {
    return mappedAccountIds;
  }

  /**
   * @param mappedAccountIds the mappedAccountId to set
   */
  public final void setMappedAccountIds(Map<Session.ServiceIdentifier, String> mappedAccountIds) {
    this.mappedAccountIds = mappedAccountIds;
  }

  /**
   * @return the serviceAccountId
   */
  public final String getServiceAccountId() {
    return serviceAccountId;
  }

  /**
   * @param serviceAccountId the serviceAccountId to set
   */
  public final void setServiceAccountId(String serviceAccountId) {
    this.serviceAccountId = serviceAccountId;
  }

  /**
   * @return transferFrom
   */
  public final boolean getCanTransferFrom() {
    return canTransferFrom;
  }

  /**
   * @param canTransferFrom the transferFrom to set
   */
  public final void setCanTransferFrom(boolean canTransferFrom) {
    this.canTransferFrom = canTransferFrom;
  }

  /**
   * @return transferTo
   */
  public final boolean getCanTransferTo() {
    return canTransferTo;
  }

  /**
   * @param canTransferTo the transferTo to set
   */
  public final void setCanTransferTo(boolean canTransferTo) {
    this.canTransferTo = canTransferTo;
  }

  /**
   * @return maskedAccountNo
   */
  public String getMaskedAccountNumber() {
    return maskedAccountNumber;
  }

  /**
   *
   * @param maskedAccountNumber
   */
  public void setMaskedAccountNumber(String maskedAccountNumber) {
    this.maskedAccountNumber = maskedAccountNumber;
  }
}
