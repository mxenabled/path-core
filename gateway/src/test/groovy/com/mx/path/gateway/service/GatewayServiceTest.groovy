package com.mx.path.gateway.service

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.gateway.service.GatewayService

import spock.lang.Specification

class GatewayServiceTest extends Specification {

  static class ConcreteService extends GatewayService {
    boolean started = false
    boolean stopped = false

    ConcreteService(ObjectMap configs) {
      super(configs)
    }

    @Override
    void start() {
      started = true
    }

    @Override
    void stop() {
      stopped = true
    }
  }

  def "describe with configurations includes class and configs"() {
    given:
    def configs = new ObjectMap()
    configs.put("timeout", "30")
    def service = new ConcreteService(configs)

    when:
    def desc = service.describe()

    then:
    desc.get("class") == "ConcreteService"
    desc.containsKey("configurations")
  }

  def "describe with empty configurations omits configurations key"() {
    given:
    def service = new ConcreteService(new ObjectMap())

    when:
    def desc = service.describe()

    then:
    desc.get("class") == "ConcreteService"
    !desc.containsKey("configurations")
  }

  def "describe(ObjectMap) fills provided map"() {
    given:
    def service = new ConcreteService(new ObjectMap())
    def description = new ObjectMap()

    when:
    service.describe(description)

    then:
    description.get("class") == "ConcreteService"
  }

  def "getGateway returns set gateway"() {
    given:
    def service = new ConcreteService(new ObjectMap())
    service.setGateway(null)

    expect:
    service.getGateway() == null
  }

  def "start and stop delegate to implementation"() {
    given:
    def service = new ConcreteService(new ObjectMap())

    when:
    service.start()
    service.stop()

    then:
    service.started
    service.stopped
  }
}
