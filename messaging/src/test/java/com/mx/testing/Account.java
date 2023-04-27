package com.mx.testing;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

@Data
public class Account extends ModelBase<Account> {
  private String id;
}
