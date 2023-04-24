package com.mx.testing.model;

import lombok.Data;

import com.mx.common.models.ModelBase;

@Data
public class Transaction extends ModelBase<Transaction> {
  private String id;
  private Double amount;
  private String description;
}
