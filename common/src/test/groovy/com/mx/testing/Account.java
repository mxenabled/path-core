package com.mx.testing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.mx.common.models.MdxBase;

@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends MdxBase<Account> {
  private String id;
}
