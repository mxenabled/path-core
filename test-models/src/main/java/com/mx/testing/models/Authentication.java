package com.mx.testing.model;

import lombok.Data;

import com.mx.path.core.common.model.ModelBase;

@Data
public class Authentication extends ModelBase<Authentication> {
  private String login;
  private String password;
  private String id;
  private String sessionKey;
}
