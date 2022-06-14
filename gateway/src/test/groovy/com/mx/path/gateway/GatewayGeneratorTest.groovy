package com.mx.path.gateway

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

import com.mx.accessors.BaseAccessor
import com.mx.accessors.account.AccountBaseAccessor
import com.mx.path.gateway.api.account.AccountGateway
import com.mx.path.gateway.api.account.TransactionGateway
import com.mx.path.gateway.context.GatewayRequestContext
import com.mx.path.model.context.RequestContext
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class GatewayGeneratorTest extends Specification {
  AccountBaseAccessor accessor
  BaseAccessor baseAccessor
  AccountGateway subject

  def setup() {
    def clientId = "clientId"
    accessor = mock(AccountBaseAccessor)
    baseAccessor = spy(new BaseAccessorImpl())
    baseAccessor.setAccounts(accessor)
    subject = AccountGateway.builder().baseAccessor(baseAccessor).clientId(clientId).build()
  }

  def "doesn't modify the original RequestContext"() {
    given:
    subject = AccountGateway.builder()
        .baseAccessor(baseAccessor)
        .clientId("clientId")
        .transactions(TransactionGateway.builder().baseAccessor(baseAccessor).build())
        .build()

    GatewayRequestContext requestContext = GatewayRequestContext.builder().clientId("client1").feature("feature1").build()
    requestContext.register()

    when:
    subject.list()

    then:
    requestContext == RequestContext.current()
    requestContext.currentAccessor == null
    requestContext.currentGateway == null
    requestContext.clientId == "client1"
    !requestContext.listOp
    requestContext.feature == "feature1"
    requestContext.model == null
  }

  def "only gets accessor once"() {
    given:
    subject = AccountGateway.builder()
        .baseAccessor(baseAccessor)
        .clientId("clientId")
        .transactions(TransactionGateway.builder().baseAccessor(baseAccessor).build())
        .build()

    GatewayRequestContext.builder().clientId("client1").feature("feature1").build().register()

    when:
    subject.list()

    then:
    verify(baseAccessor, times(1)).accounts() || true
  }
}
