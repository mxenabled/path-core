package com.mx.path.gateway.configuration

import com.google.common.eventbus.Subscribe
import com.mx.testing.gateway.BaseGateway

import spock.lang.Specification

class ConfiguratorObserverTest extends Specification {
  ConfiguratorObserver subject
  def configurator

  class AfterGatewayInitialized extends Configurator<BaseGateway> {
  }

  class TestGatewayListener {
    def static gatewayInitializedInvokeCount = 0
    def static facilitiesInitializedInvokeCount = 0

    @Subscribe
    public void afterGatewaysInitialized(ConfiguratorObserver.GatewaysInitializedEvent event) {
      gatewayInitializedInvokeCount++
    }

    @Subscribe
    public void afterFacilitiesInitialized(ConfiguratorObserver.ClientFacilitiesInitializedEvent event) {
      facilitiesInitializedInvokeCount++
    }
  }

  def setup() {
    configurator = new AfterGatewayInitialized()
    subject = new ConfiguratorObserver(configurator)
  }

  def cleanup() {
    try {
      AfterGatewayInitialized.currentInstance().close()
    } catch (Exception e) {}
  }

  def "notifyClientFacilitiesInitialized"() {
    given:
    def listener = new TestGatewayListener()

    subject.registerListener(listener)
    subject.registerListener(listener) // should on result in one invocation

    when:
    subject.notifyClientFacilitiesInitialized("clientA")

    then:
    listener.facilitiesInitializedInvokeCount == 1
  }

  def "notifyClientFacilitiesInitialized blocks"() {
    given:
    def invokedCount = 0

    subject.registerClientFacilitiesInitialized({ configurator, gateways ->
      invokedCount++
    })
    subject.registerClientFacilitiesInitialized({ configurator, gateways ->
      invokedCount++
    }) // both blocks should be invoked

    when:
    subject.notifyClientFacilitiesInitialized("clientA")

    then:
    invokedCount == 2
  }

  def "notifyGatewaysInitialized"() {
    given:
    def listener = new TestGatewayListener()

    subject.registerListener(listener)
    subject.registerListener(listener) // should on result in one invocation

    when:
    subject.notifyGatewaysInitialized(new HashMap<String, BaseGateway>())

    then:
    listener.gatewayInitializedInvokeCount == 1
  }

  def "notifyGatewaysInitialized blocks"() {
    given:
    def invokedCount = 0

    subject.registerGatewaysInitialized({ configurator, gateways ->
      invokedCount++
    })
    subject.registerGatewaysInitialized({ configurator, gateways ->
      invokedCount++
    }) // both blocks should be invoked

    when:
    subject.notifyGatewaysInitialized(new HashMap<String, BaseGateway>())

    then:
    invokedCount == 2
  }
}
