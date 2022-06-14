package com.mx.models.ach_transfer;

import com.mx.models.Internal;
import com.mx.models.MdxBase;

public final class AchAccount extends MdxBase<AchAccount> {
  private String id;
  private String type;
  private String status;
  private String accountId;
  private Long createdAt;
  private String bankName;
  private String name;
  private String nickname;
  private String accountNumber;
  private String routingNumber;

  @Internal
  private boolean transferFrom;
  @Internal
  private boolean transferTo;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getRoutingNumber() {
    return routingNumber;
  }

  public void setRoutingNumber(String routingNumber) {
    this.routingNumber = routingNumber;
  }

  public boolean isTransferFrom() {
    return transferFrom;
  }

  public void setTransferFrom(boolean transferFrom) {
    this.transferFrom = transferFrom;
  }

  public boolean isTransferTo() {
    return transferTo;
  }

  public void setTransferTo(boolean transferTo) {
    this.transferTo = transferTo;
  }
}
