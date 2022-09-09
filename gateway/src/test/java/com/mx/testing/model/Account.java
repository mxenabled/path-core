package com.mx.testing.model;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class Account extends MdxBase<Account> {
  private String id;
  private String description;
  private String type;
  private Double balance;
}
