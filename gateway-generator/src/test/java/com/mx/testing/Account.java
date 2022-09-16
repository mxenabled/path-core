package com.mx.testing;

import lombok.Data;

import com.mx.common.models.MdxBase;

@Data
public class Account extends MdxBase<Account> {
  private String id;
}
