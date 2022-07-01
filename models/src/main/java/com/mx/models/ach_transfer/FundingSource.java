package com.mx.models.ach_transfer;

import com.google.gson.annotations.SerializedName;
import com.mx.models.Internal;
import com.mx.models.MdxBase;
import com.mx.models.UserIdProvider;

@Deprecated // This is being replaced by: https://developer.mx.com/drafts/mdx/ach_transfer/#mdx-ach-transfer
public final class FundingSource extends MdxBase<FundingSource> {

  private String id;
  private String type;
  @SerializedName("routing_number")
  private String routingNumber;
  @SerializedName("account_number")
  private String accountNumber;
  private String status;
  @SerializedName("account_guid")
  private String accountGuid;
  @SerializedName("created_at")
  private Long createdAt;
  @SerializedName("bank_name")
  private String bankName;
  private String nickname;

  @Internal
  private boolean isExternal;

  public FundingSource() {
    UserIdProvider.setUserId(this);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRoutingNumber() {
    return routingNumber;
  }

  public void setRoutingNumber(String routingNumber) {
    this.routingNumber = routingNumber;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAccountGuid() {
    return accountGuid;
  }

  public void setAccountGuid(String accountGuid) {
    this.accountGuid = accountGuid;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public boolean isExternal() {
    return isExternal;
  }

  public void setExternal(boolean external) {
    isExternal = external;
  }
}
