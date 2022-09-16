package com.mx.testing.model;

import lombok.Data;

import com.mx.common.models.MdxBase;

@Data
public class Authentication extends MdxBase<Authentication> {
  private String login;
  private String password;
  private String id;
  private String sessionKey;
}
