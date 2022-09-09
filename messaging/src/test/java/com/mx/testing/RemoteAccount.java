package com.mx.testing;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class RemoteAccount extends MdxBase<RemoteAccount> {
  private String id;
  private String description;
  private String accountNumber;
  private String type;
  private double balance;
}
