package com.mx.testing.models.v20240101;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

@Data
public class Account extends ModelBase<Account> {
  private String id;
  private String description;
  private String accountType;
  private Double balance;
}
