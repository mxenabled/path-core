package com.mx.path.gateway.context

import static org.mockito.Mockito.mock

import com.mx.path.gateway.Gateway
import com.mx.path.model.context.RequestContext
import com.mx.testing.AccountAccessorImpl
import com.mx.testing.gateway.BaseGateway
import com.mx.testing.model.Account

import spock.lang.Specification

class GatewayRequestContextTest extends Specification {
  def cleanup() {
    RequestContext.clear()
    GatewayRequestContext.clear()
  }

  def "fromRequestContext"() {
    given:
    def gatewayRequestContext = GatewayRequestContext.builder().build()
    def requestContext = RequestContext.builder().build()

    when: "passing in a GatewayRequestContext"
    def result = GatewayRequestContext.fromRequestContext(gatewayRequestContext)

    then:
    result == gatewayRequestContext

    when: "passing in a RequestContext"
    result = GatewayRequestContext.fromRequestContext(requestContext)

    then:
    !result.is(requestContext)
    result instanceof GatewayRequestContext

    when: "passing in null"
    result = GatewayRequestContext.fromRequestContext(null)

    then:
    result != null
    result instanceof GatewayRequestContext

    when: "converting a registered RequestContext to a GatewayRequestContext and registering it"
    requestContext = RequestContext.builder().clientGuid("clientAF").build()
    requestContext.register()

    gatewayRequestContext = GatewayRequestContext.fromRequestContext(requestContext)
    gatewayRequestContext.register()

    then:
    RequestContext.current().is(gatewayRequestContext)
    GatewayRequestContext.current().is(gatewayRequestContext)
    gatewayRequestContext.getClientGuid() == "clientAF"
  }

  def "current"() {
    given:
    def gateway = mock(Gateway)
    def context = GatewayRequestContext.builder().gateway(gateway).build()

    when:
    context.register()

    then:
    GatewayRequestContext.current() == context
    GatewayRequestContext.current().getGateway() == gateway
  }

  def "reset"() {
    given:
    def context = GatewayRequestContext.builder().build()

    when:
    context.register()

    then:
    GatewayRequestContext.current() == context

    when:
    GatewayRequestContext.clear()

    then:
    GatewayRequestContext.current() == null
  }

  def "builder"() {
    given:
    def baseGateway = new BaseGateway()
    def accountAccessor = new AccountAccessorImpl()
    def gateway = mock(Gateway)
    when:
    def subject = GatewayRequestContext.builder()
        .clientGuid("clientAF")
        .clientId("client1")
        .feature("featureA")
        .header("h2" , "v2")
        .originatingIP("127.0.0.1")
        .parameter("p2", "v2")
        .path("/accounts")
        .sessionTraceId("abcde")
        .userGuid("userAF")
        .withHeaders({ h -> h.put("h1", "v1") })
        .withParameters({ p -> p.put("p1", "v1") })
        .gateway(gateway)
        .currentAccessor(accountAccessor)
        .currentGateway(baseGateway)
        .listOp(false)
        .model(Account)
        .op("get")
        .build()

    then:
    subject.getClientGuid() == "clientAF"
    subject.getClientId() == "client1"
    subject.getFeature() == "featureA"
    subject.getOriginatingIP() == "127.0.0.1"
    subject.getPath() == "/accounts"
    subject.getSessionTraceId() == "abcde"
    subject.getUserGuid() == "userAF"
    subject.getHeaders().get("h2") == "v2"
    subject.getHeaders().get("h1") == "v1"
    subject.getParams().get("p1") == "v1"
    subject.getParams().get("p2") == "v2"
    subject.getGateway() == gateway
    subject.getCurrentAccessor() == accountAccessor
    subject.getCurrentGateway() == baseGateway
    !subject.isListOp()
    subject.getModel() == Account
    subject.getOp() == "get"
  }

  def "withSelfClearing"() {
    given:
    def context = GatewayRequestContext.builder().build()

    when: "context already set"
    context.register()

    GatewayRequestContext.withSelfClearing("client1", {c ->
      assert context == c, "Must pass current() context"
    })

    then:
    true

    when:
    GatewayRequestContext.clear()

    GatewayRequestContext.withSelfClearing("client1", {c ->
      assert c.getClientId() == "client1", "Client ID not set"
      assert context != c, "Must set new current() context"
    })

    then:
    true
  }

  def "setters"() {
    given:
    def subject = GatewayRequestContext.builder().build()
    def baseGateway = new BaseGateway()
    def accountAccessor = new AccountAccessorImpl()
    def gateway = mock(Gateway)

    when:
    subject.setClientGuid("clientAF")
    subject.setClientId("client1")
    subject.setFeature("featureA")
    subject.setOriginatingIP("127.0.0.1")
    subject.setPath("/accounts")
    subject.setSessionTraceId("abcde")
    subject.setUserGuid("userAF")
    subject.getHeaders().put("h2", "v2")
    subject.getHeaders().put("h1", "v1")
    subject.getParams().put("p1", "v1")
    subject.getParams().put("p2", "v2")
    subject.setGateway(gateway)
    subject.setCurrentAccessor(accountAccessor)
    subject.setCurrentGateway(baseGateway)
    subject.setListOp(true)
    subject.setModel(Account)
    subject.setOp("list")

    then:
    subject.getClientGuid() == "clientAF"
    subject.getClientId() == "client1"
    subject.getFeature() == "featureA"
    subject.getOriginatingIP() == "127.0.0.1"
    subject.getPath() == "/accounts"
    subject.getSessionTraceId() == "abcde"
    subject.getUserGuid() == "userAF"
    subject.getHeaders().get("h2") == "v2"
    subject.getHeaders().get("h1") == "v1"
    subject.getParams().get("p1") == "v1"
    subject.getParams().get("p2") == "v2"
    subject.getGateway() == gateway
    subject.getCurrentAccessor() == accountAccessor
    subject.getCurrentGateway() == baseGateway
    subject.isListOp()
    subject.getModel() == Account
    subject.getOp() == "list"
  }

  def "toBuilder"() {
    given:
    def subject = GatewayRequestContext.builder().build()
    def baseGateway = new BaseGateway()
    def accountAccessor = new AccountAccessorImpl()
    def gateway = mock(Gateway)

    when:
    subject.setClientGuid("clientAF")
    subject.setClientId("client1")
    subject.setFeature("featureA")
    subject.setOriginatingIP("127.0.0.1")
    subject.setPath("/accounts")
    subject.setSessionTraceId("abcde")
    subject.setUserGuid("userAF")
    subject.getHeaders().put("h2", "v2")
    subject.getHeaders().put("h1", "v1")
    subject.getParams().put("p1", "v1")
    subject.getParams().put("p2", "v2")
    subject.setGateway(gateway)
    subject.setCurrentAccessor(accountAccessor)
    subject.setCurrentGateway(baseGateway)
    subject.setListOp(true)
    subject.setModel(Account)
    subject.setOp("list")

    def result = subject.toBuilder().build()
    then:
    result.getClientGuid() == "clientAF"
    result.getClientId() == "client1"
    result.getFeature() == "featureA"
    result.getOriginatingIP() == "127.0.0.1"
    result.getPath() == "/accounts"
    result.getSessionTraceId() == "abcde"
    result.getUserGuid() == "userAF"
    result.getHeaders().get("h2") == "v2"
    result.getHeaders().get("h1") == "v1"
    result.getParams().get("p1") == "v1"
    result.getParams().get("p2") == "v2"
    result.getGateway() == gateway
    result.getCurrentAccessor() == accountAccessor
    result.getCurrentGateway() == baseGateway
    result.isListOp()
    result.getModel() == Account
    result.getOp() == "list"
  }
}
