package com.mx.gateway

import com.mx.path.core.common.collection.ObjectMap
import com.mx.testing.GatewayServiceImpl
import com.mx.testing.gateway.api.AccountGateway

import spock.lang.Specification

class GatewayServiceTest extends Specification {

  def "ctor"() {
    given:
    def gateway = new AccountGateway()
    def config = new ObjectMap()

    when:
    def subject = new GatewayServiceImpl(config)
    subject.setGateway(gateway)

    then:
    subject.getGateway() == gateway
    subject.getConfigurations() == config
  }

  def "describe"() {
    given:
    def gateway = new AccountGateway()
    def config = new ObjectMap()
    def subject = new GatewayServiceImpl(config)

    when:
    def description = subject.describe()

    then:
    description.getMap("configuration") == null

    when:
    config.put("key", "value")
    description = subject.describe()

    then:
    description.getMap("configurations").getAsString("key") == "value"
  }
}
