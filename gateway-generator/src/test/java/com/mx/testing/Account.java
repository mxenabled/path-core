package com.mx.testing;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Account extends TestMdxBase<Account> {
  private String id;
}
