package com.mx.testing.model;

import lombok.Data;

import com.mx.models.MdxBase;

@Data
public class Authentication extends MdxBase<Authentication> {
  private String login;
  private String password;
  private String id;
  private String sessionKey;
}
