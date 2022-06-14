package com.mx.models.transfer.options;

import lombok.Data;

@Data
public class FrequencyListOptions {
  @Deprecated
  private String flow;
  private String transferType;
}
