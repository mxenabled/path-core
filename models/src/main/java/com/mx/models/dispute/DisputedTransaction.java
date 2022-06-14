package com.mx.models.dispute;

import javax.xml.bind.annotation.XmlElement;

import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

/**
 * Represents an MDX disputed transaction. XmlElements assigned so that it can be demarshalled from MDXv5 XML.
 */
public class DisputedTransaction extends MdxBase<DisputedTransaction> {

  @XmlElement(name = "amount")
  private Double amount;
  @XmlElement(name = "dispute_id")
  private String disputeId;
  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "merchant_name")
  private String merchantName;
  @XmlElement(name = "transacted_on")
  private String transactedOn;
  @XmlElement(name = "transaction_id")
  private String transactionId;

  public DisputedTransaction() {
    UserIdProvider.setUserId(this);
  }

  public final Double getAmount() {
    return amount;
  }

  public final void setAmount(Double newAmount) {
    this.amount = newAmount;
  }

  public final String getDisputeId() {
    return disputeId;
  }

  public final void setDisputeId(String newDisputeId) {
    this.disputeId = newDisputeId;
  }

  public final String getId() {
    return id;
  }

  public final void setId(String newId) {
    this.id = newId;
  }

  public final String getMerchantName() {
    return merchantName;
  }

  public final void setMerchantName(String newMerchantName) {
    this.merchantName = newMerchantName;
  }

  public final String getTransactedOn() {
    return transactedOn;
  }

  public final void setTransactedOn(String newTransactedOn) {
    this.transactedOn = newTransactedOn;
  }

  public final String getTransactionId() {
    return transactionId;
  }

  public final void setTransactionId(String newTransactionId) {
    this.transactionId = newTransactionId;
  }
}
