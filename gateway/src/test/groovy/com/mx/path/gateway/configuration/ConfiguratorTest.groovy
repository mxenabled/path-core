package com.mx.path.gateway.configuration

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

import com.mx.path.gateway.Gateway
import com.mx.testing.gateway.BaseGateway
import com.mx.testing.gateway.TestAccountGateway
import com.mx.testing.gateway.TestGateway
import com.mx.testing.gateway.TestIdGateway

import org.mockito.ArgumentCaptor

import spock.lang.Specification

class ConfiguratorTest extends Specification {
  class TestConfigurator extends Configurator<TestGateway> {
  }

  ConfiguratorObserver observer
  TestConfigurator subject

  def setup() {
    subject = new TestConfigurator()
    observer = spy(new ConfiguratorObserver(subject))
    subject.setObserver(observer)
  }

  def "invokes after gateways initialized listeners"() {
    given:
    def yaml =
        "client:\n" +
        "  accessor:\n" +
        "    class: com.mx.testing.accessors.BaseAccessor\n" +
        "    scope: singleton\n" +
        "  gateways:\n" +
        "    id: {}\n" +
        "    accounts: {}\n"

    def gatewayCaptor = ArgumentCaptor.forClass(Gateway)

    when:
    Map<String, TestGateway> gateways = subject.buildFromYaml(yaml)

    then:
    gateways.get("client") != null
    gateways.get("client")
    verify(observer, times(1)).notifyGatewaysInitialized(any())
    verify(observer, times(1)).notifyClientGatewayInitialized(eq("client"), any(TestGateway))
    verify(observer, times(3)).notifyGatewayInitialized(gatewayCaptor.capture())

    gatewayCaptor.allValues.find { it.getClass() == TestGateway }
    gatewayCaptor.allValues.find { it.getClass() == TestIdGateway }
    gatewayCaptor.allValues.find { it.getClass() == TestAccountGateway }
  }
}
