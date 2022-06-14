package com.mx.testing;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.BaseGateway;
import com.mx.path.gateway.api.account.AccountGateway;
import com.mx.path.gateway.api.id.IdGateway;

@SuperBuilder
public class BaseGatewayImpl extends BaseGateway {
  public BaseGatewayImpl() {
    super();
  }

  @Getter
  @Setter
  private IdGateway id;

  @Getter
  @Setter
  private AccountGateway accounts;
}
