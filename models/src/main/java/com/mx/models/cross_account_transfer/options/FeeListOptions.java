package com.mx.models.cross_account_transfer.options;

import lombok.Data;

@Data
public class FeeListOptions {
  private String accountTypeId;
  private Integer accountTypeNumber;
  private Number amount;
  private String destinationId;
  private String fromAccountId;
}
