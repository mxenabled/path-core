package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.configuration.RootGateway;

@SuperBuilder
@RootGateway
public class TestGateway extends BaseGateway {
  private TestAccountGateway accounts;
  private TestIdGateway id;
}
