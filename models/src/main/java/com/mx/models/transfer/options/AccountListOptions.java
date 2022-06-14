package com.mx.models.transfer.options;

import lombok.Data;

@Data
public class AccountListOptions {
  private String accountId;
  @Deprecated
  private String flow;
  private String listType;
  private String transferType;
}
