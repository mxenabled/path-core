package com.mx.models.transfer.options;

import lombok.Data;

@Data
public class FeeListOptions {
  private Number amount;
  private String fromAccountId;
  private String toAccountId;
}
