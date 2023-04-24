package com.mx.testing;

import lombok.Data;

import com.mx.common.models.ModelBase;

@Data
public class Account extends ModelBase<Account> {
  private String id;
}
