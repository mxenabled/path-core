package com.mx.testing.model;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

/**
 * Test class for transactions.
 */
@Data
public class Transaction extends ModelBase<Transaction> {
  /**
   * -- GETTER --
   * Return transaction id.
   *
   * @return transaction id
   * -- SETTER --
   * Set transaction id.
   *
   * @param id id to set
   */
  private String id;

  /**
   * -- GETTER --
   * Return transaction amount.
   *
   * @return transaction amount
   * -- SETTER --
   * Set transaction amount.
   *
   * @param amount amount to set
   */
  private Double amount;

  /**
   * -- GETTER --
   * Return transaction description.
   *
   * @return transaction description
   * -- SETTER --
   * Set transaction description.
   *
   * @param description description to set
   */
  private String description;
}
