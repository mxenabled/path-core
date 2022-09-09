package com.mx.testing;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class Account extends MdxBase<Account> {
  private String id;
}
