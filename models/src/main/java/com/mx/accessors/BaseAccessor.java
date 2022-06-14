package com.mx.accessors;

import lombok.AccessLevel;
import lombok.Getter;

import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.accessors.ach_transfer.AchTransferBaseAccessor;
import com.mx.accessors.authorization.AuthorizationBaseAccessor;
import com.mx.accessors.credit_report.CreditReportBaseAccessor;
import com.mx.accessors.cross_account_transfer.CrossAccountTransferBaseAccessor;
import com.mx.accessors.document.DocumentBaseAccessor;
import com.mx.accessors.id.IdBaseAccessor;
import com.mx.accessors.location.LocationBaseAccessor;
import com.mx.accessors.managed_card.ManagedCardBaseAccessor;
import com.mx.accessors.origination.OriginationBaseAccessor;
import com.mx.accessors.payment.PaymentBaseAccessor;
import com.mx.accessors.payout.PayoutBaseAccessor;
import com.mx.accessors.profile.ProfileBaseAccessor;
import com.mx.accessors.remote_deposit.RemoteDepositBaseAccessor;
import com.mx.accessors.transfer.TransferBaseAccessor;
import com.mx.common.gateway.GatewayAPI;

/**
 * Base MX Path Gateway accessor
 */
@API(description = "Base Gateway Accessor that serves as the main entrypoint to all other Gateway Accessors", specificationUrl = "https://developer.mx.com/drafts/mdx/overview/#what-is-helios")
public abstract class BaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AchTransferBaseAccessor achTransfers;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AccountBaseAccessor accounts;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AuthorizationBaseAccessor authorizations;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private CreditReportBaseAccessor creditReports;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  @Deprecated
  private com.mx.accessors.ach_transfer.TransferBaseAccessor deprecatedAchTransfers;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private DocumentBaseAccessor documents;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private IdBaseAccessor id;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private LocationBaseAccessor locations;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private ManagedCardBaseAccessor managedCards;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private OriginationBaseAccessor originations;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private PaymentBaseAccessor payments;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private PayoutBaseAccessor payouts;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private ProfileBaseAccessor profiles;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private RemoteDepositBaseAccessor remoteDeposits;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private StatusBaseAccessor status;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private TransferBaseAccessor transfers;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private CrossAccountTransferBaseAccessor crossAccount;

  public BaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Accessor for ACH transfer operations
   * @return accessor
   */
  @API
  public AchTransferBaseAccessor achTransfers() {
    if (achTransfers != null) {
      return achTransfers;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set ACH transfers accessor
   * @param achTransfers
   */
  public void setAchTransfers(AchTransferBaseAccessor achTransfers) {
    this.achTransfers = achTransfers;
  }

  /**
   * Accessor for account operations
   * @return accessor
   */
  @API
  public AccountBaseAccessor accounts() {
    if (accounts != null) {
      return accounts;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set accounts accessor
   * @param accounts
   */
  public void setAccounts(AccountBaseAccessor accounts) {
    this.accounts = accounts;
  }

  /**
   * Accessor for authorization operations
   * @return accessor
   */
  @API
  public AuthorizationBaseAccessor authorizations() {
    if (authorizations != null) {
      return authorizations;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set authorizations accessor
   * @param authorizations
   */
  public void setAuthorizations(AuthorizationBaseAccessor authorizations) {
    this.authorizations = authorizations;
  }

  /**
   * Accessor for account operations
   * @return accessor
   */
  @API
  public CreditReportBaseAccessor creditReports() {
    if (creditReports != null) {
      return creditReports;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set credit report accessor
   * @param creditReports
   */
  public void setCreditReports(CreditReportBaseAccessor creditReports) {
    this.creditReports = creditReports;
  }

  /**
   * Deprecated accessor for ACH transfer operations
   * @return accessor
   */
  @API
  @Deprecated
  public com.mx.accessors.ach_transfer.TransferBaseAccessor deprecatedAchTransfers() {
    if (deprecatedAchTransfers != null) {
      return deprecatedAchTransfers;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set deprecated ACH transfers accessor
   * @param deprecatedAchTransfers
   */
  @Deprecated
  public void setDeprecatedAchTransfers(com.mx.accessors.ach_transfer.TransferBaseAccessor deprecatedAchTransfers) {
    this.deprecatedAchTransfers = deprecatedAchTransfers;
  }

  /**
   * Accessor for account operations
   * @return accessor
   */
  @API
  public DocumentBaseAccessor documents() {
    if (documents != null) {
      return documents;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set documents accessor
   * @param documents
   */
  public void setDocuments(DocumentBaseAccessor documents) {
    this.documents = documents;
  }

  /**
   * Accessor for id operations
   * @return accessor
   */
  @API
  public IdBaseAccessor id() {
    if (id != null) {
      return id;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set id accessor
   * @param id
   */
  public void setId(IdBaseAccessor id) {
    this.id = id;
  }

  /**
   * Accessor for location operations
   * @return accessor
   */
  @API
  public LocationBaseAccessor locations() {
    if (locations != null) {
      return locations;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set locations accessor
   * @param locations
   */
  public void setLocations(LocationBaseAccessor locations) {
    this.locations = locations;
  }

  /**
   * Accessor for managed cards operations
   * @return accessor
   */
  @API
  public ManagedCardBaseAccessor managedCards() {
    if (managedCards != null) {
      return managedCards;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set managed cards accessor
   * @param managedCards
   */
  public void setManagedCards(ManagedCardBaseAccessor managedCards) {
    this.managedCards = managedCards;
  }

  /**
   * Accessor for originations operations
   * @return accessor
   */
  @API
  public OriginationBaseAccessor originations() {
    if (originations != null) {
      return originations;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set originations accessor
   * @param originations
   */
  public void setOriginations(OriginationBaseAccessor originations) {
    this.originations = originations;
  }

  /**
   * Accessor for payment operations
   * @return accessor
   */
  @API
  public PaymentBaseAccessor payments() {
    if (payments != null) {
      return payments;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set payments accessor
   * @param payments
   */
  public void setPayments(PaymentBaseAccessor payments) {
    this.payments = payments;
  }

  /**
   * Accessor for payout operations
   * @return accessor
   */
  @API
  public PayoutBaseAccessor payouts() {
    if (payouts != null) {
      return payouts;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set payouts accessor
   * @param payouts
   */
  public void setPayouts(PayoutBaseAccessor payouts) {
    this.payouts = payouts;
  }

  /**
   * Accessor for profile operations
   * @return accessor
   */
  @API
  public ProfileBaseAccessor profiles() {
    if (profiles != null) {
      return profiles;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set profiles accessor
   * @param profiles
   */
  public void setProfiles(ProfileBaseAccessor profiles) {
    this.profiles = profiles;
  }

  /**
   * Accessor for status operations
   * @return accessor
   */
  @API
  public StatusBaseAccessor status() {
    if (status != null) {
      return status;
    }

    return new StatusDefaultAccessor(getConfiguration());
  }

  /**
   * Set status accessor
   * @param status
   */
  public void setStatus(StatusBaseAccessor status) {
    this.status = status;
  }

  /**
   * Accessor for remote deposit operations
   * @return
   */
  @API
  public RemoteDepositBaseAccessor remoteDeposits() {
    if (remoteDeposits != null) {
      return remoteDeposits;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set remote deposits accessor
   * @param remoteDeposits
   */
  public void setRemoteDeposits(RemoteDepositBaseAccessor remoteDeposits) {
    this.remoteDeposits = remoteDeposits;
  }

  /**
   * Accessor for transfer operations
   * @return accessor
   */
  @API
  public TransferBaseAccessor transfers() {
    if (transfers != null) {
      return transfers;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set transfers accessor
   * @param transfers
   */
  public void setTransfers(TransferBaseAccessor transfers) {
    this.transfers = transfers;
  }

  /**
   * Accessor for cross-account transfer operations
   * @return accessor
   */
  @API
  public CrossAccountTransferBaseAccessor crossAccount() {
    if (crossAccount != null) {
      return crossAccount;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set cross-account transfer accessor
   * @param crossAccount
   */
  public void setCrossAccount(CrossAccountTransferBaseAccessor crossAccount) {
    this.crossAccount = crossAccount;
  }
}
