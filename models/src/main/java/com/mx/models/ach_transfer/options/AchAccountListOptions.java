package com.mx.models.ach_transfer.options;

import lombok.Data;

@Data
public class AchAccountListOptions {
  private String transferType;
  private String listType;
  private String accountId;
  private String achAccountId;
}
