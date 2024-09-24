package com.mx.testing.model;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

/**
 * Test class for authentication.
 */
@Data
public class Authentication extends ModelBase<Authentication> {

  /**
   * -- GETTER --
   * Return authentication login.
   *
   * @return authentication login
   * -- SETTER --
   * Set authentication login.
   *
   * @param login login to set
   */
  private String login;

  /**
   * -- GETTER --
   * Return authentication password.
   *
   * @return authentication password
   * -- SETTER --
   * Set authentication password.
   *
   * @param password password to set
   */
  private String password;

  /**
   * -- GETTER --
   * Return authentication id.
   *
   * @return authentication id
   * -- SETTER --
   * Set authentication id.
   *
   * @param id id to set
   */
  private String id;

  /**
   * -- GETTER --
   * Return authentication sessionKey.
   *
   * @return authentication sessionKey
   * -- SETTER --
   * Set authentication sessionKey.
   *
   * @param sessionKey sessionKey to set
   */
  private String sessionKey;
}
