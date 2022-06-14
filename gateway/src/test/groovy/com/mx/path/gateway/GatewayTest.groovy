package com.mx.path.gateway

import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.BaseAccessor
import com.mx.path.gateway.api.Gateway
import com.mx.path.gateway.api.account.AccountGateway
import com.mx.path.gateway.api.account.TransactionGateway
import com.mx.path.gateway.api.id.IdGateway
import com.mx.testing.BaseAccessorImpl
import com.mx.testing.GatewayBehaviorImpl

import spock.lang.Specification

class GatewayTest extends Specification {
  BaseAccessor accessor
  def setup() {
    accessor = new BaseAccessorImpl(AccessorConfiguration.builder().clientId("clientId").build())
  }

  def "builder"() {
    when:
    def gateway = Gateway.builder()
        .clientId("clientId")
        .baseAccessor(accessor)
        .accounts(new AccountGateway())
        .id(new IdGateway())
        .build()

    then:
    gateway.accounts().class == AccountGateway.class
    gateway.id().class == IdGateway.class
    gateway.clientId == "clientId"
    gateway.gateways().size() == 2
    gateway.baseAccessor == accessor
  }

  def "ctor"() {
    when:
    def gateway = new Gateway("clientId")

    then:
    gateway.getClientId() == "clientId"
  }

  def "describe"() {
    given:
    def gateway = Gateway.builder()
        .clientId("clientId")
        .baseAccessor(accessor)
        .accounts(AccountGateway.builder().baseAccessor(accessor)
        .behavior(new GatewayBehaviorImpl())
        .transactions(TransactionGateway.builder().baseAccessor(accessor).build())
        .build())
        .id(IdGateway.builder().baseAccessor(accessor).build())
        .build()

    when:
    def description = gateway.describe()

    then:
    description.getMap("gateways").getMap("id") != null
  }
}
