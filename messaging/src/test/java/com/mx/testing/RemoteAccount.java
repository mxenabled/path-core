package com.mx.testing;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

@Data
public class RemoteAccount extends ModelBase<RemoteAccount> {
  private String id;
  private String description;
  private String accountNumber;
  private String type;
  private double balance;
}
