package com.mx.testing;

import lombok.Data;

import com.mx.common.models.ModelBase;

@Data
public class RemoteAccount extends ModelBase<RemoteAccount> {
  private String id;
  private String description;
  private String accountNumber;
  private String type;
  private double balance;
}
