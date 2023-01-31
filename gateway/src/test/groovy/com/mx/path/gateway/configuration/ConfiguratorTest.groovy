package com.mx.path.gateway.configuration


import com.mx.testing.gateway.BaseGateway

import spock.lang.Specification

class ConfiguratorTest extends Specification {
  class AfterGatewayInitializedConfigurator extends Configurator<BaseGateway> {
  }

  class TestGatewayInitializer implements GatewayInitializer<BaseGateway> {
    def hookCalledWith
    def invokeCount = 0

    @Override
    void afterGatewaysInitialized(Map<String, BaseGateway> gateways) {
      hookCalledWith = gateways
      invokeCount++
    }
  }

  def "invokes afterGatewaysInitialized hook"() {
    given:
    def yaml =
        "client:\n" +
        "  accessor:\n" +
        "    class: com.mx.testing.accessors.BaseAccessor\n" +
        "    scope: singleton\n" +
        "  gateways:\n" +
        "    accounts: {}\n"

    def initializer = new TestGatewayInitializer()
    def subject = new AfterGatewayInitializedConfigurator()
    subject.setInitializer(initializer)

    when:
    def gateways = subject.buildFromYaml(yaml)

    then:
    initializer.getHookCalledWith() == gateways
    initializer.invokeCount == 1
  }
}
