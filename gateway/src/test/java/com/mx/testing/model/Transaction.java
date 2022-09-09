package com.mx.testing.model;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class Transaction extends MdxBase<Transaction> {
  private String id;
  private Double amount;
  private String description;
}
