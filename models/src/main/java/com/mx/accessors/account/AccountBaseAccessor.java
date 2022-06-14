package com.mx.accessors.account;

import lombok.AccessLevel;
import lombok.Getter;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.account.Account;

/**
 * Accessor for account operations
 */
@GatewayClass
@API(description = "Access to user accounts", specificationUrl = "https://developer.mx.com/drafts/mdx/accounts/#mdx-account")
public abstract class AccountBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AccountNumberBaseAccessor accountNumbers;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AccountOwnerBaseAccessor accountOwners;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private TransactionBaseAccessor transactions;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private RepaymentBaseAccessor repayments;

  public AccountBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Account number accessor
   * @return
   */
  @API(description = "Access account numbers")
  public AccountNumberBaseAccessor accountNumbers() {
    if (accountNumbers != null) {
      return accountNumbers;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Account owner accessor
   * @return
   */
  @API(description = "Access account owners")
  public AccountOwnerBaseAccessor accountOwners() {
    if (accountOwners != null) {
      return accountOwners;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Transaction accessor
   * @return
   */
  @API(description = "Access account's transactions")
  public TransactionBaseAccessor transactions() {
    if (transactions != null) {
      return transactions;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Repayment accessor
   * @return
   */
  @API(description = "Access account's repayments")
  public RepaymentBaseAccessor repayments() {
    if (repayments != null) {
      return repayments;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set account number accessor
   * @param accountNumbers
   */
  public void setAccountNumbers(AccountNumberBaseAccessor accountNumbers) {
    this.accountNumbers = accountNumbers;
  }

  /**
   * Set account owner accessor
   * @param accountOwners
   */
  public void setAccountOwners(AccountOwnerBaseAccessor accountOwners) {
    this.accountOwners = accountOwners;
  }

  /**
   * Set transaction accessor
   * @param transactions
   */
  public void setTransactions(TransactionBaseAccessor transactions) {
    this.transactions = transactions;
  }

  /**
   * Set repayment accessor
   * @param repayments
   */
  public void setRepayments(RepaymentBaseAccessor repayments) {
    this.repayments = repayments;
  }

  /**
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "Get all user's account")
  public AccessorResponse<MdxList<Account>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create an account
   * @param account
   * @return
   */
  @GatewayAPI
  @API(description = "Create account")
  public AccessorResponse<Account> create(Account account) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete an account
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete account")
  public AccessorResponse<Account> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get an account by id
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get an account by id")
  public AccessorResponse<Account> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update account
   * @param account
   * @return
   */
  @GatewayAPI
  @API(description = "Update given account")
  public AccessorResponse<Account> update(String id, Account account) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
