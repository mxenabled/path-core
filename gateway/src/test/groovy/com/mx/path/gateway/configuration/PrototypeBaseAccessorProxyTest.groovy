package com.mx.path.gateway.configuration

import com.mx.accessors.AccessorConfiguration
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxyPrototype
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class PrototypeBaseAccessorProxyTest extends Specification {

  def "build returns different instance"() {
    given:
    def subject = new BaseAccessorProxyPrototype(AccessorConfiguration.builder().clientId("client1").build(), BaseAccessorImpl.class);

    when:
    def instance = subject.build()

    then:
    instance != subject.build()
  }
}
