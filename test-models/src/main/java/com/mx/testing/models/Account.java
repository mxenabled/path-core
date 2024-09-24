package com.mx.testing.model;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

/**
 * Basic account model for tests.
 */
@Data
public class Account extends ModelBase<Account> {

  /**
   * -- GETTER --
   * Return account id.
   *
   * @return account id
   * -- SETTER --
   * Set account id.
   *
   * @param id id to set
   */
  private String id;

  /**
   * -- GETTER --
   * Return account description.
   *
   * @return account description
   * -- SETTER --
   * Set account description.
   *
   * @param description description to set
   */
  private String description;

  /**
   * -- GETTER --
   * Return account type.
   *
   * @return account type
   * -- SETTER --
   * Set account type.
   *
   * @param type type to set
   */
  private String type;

  /**
   * -- GETTER --
   * Return account balance.
   *
   * @return account balance
   * -- SETTER --
   * Set account balance.
   *
   * @param balance balance to set
   */
  private Double balance;
}
