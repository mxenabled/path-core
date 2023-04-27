package com.mx.testing.model;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

@Data
public class Transaction extends ModelBase<Transaction> {
  private String id;
  private Double amount;
  private String description;
}
