package com.mx.gateway


import com.mx.path.gateway.accessor.AccessorConfiguration
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxySingleton
import com.mx.testing.BaseAccessorImpl
import com.mx.testing.accessors.BaseAccessor

import spock.lang.Specification

class SingletonBaseAccessorProxyTest extends Specification {
  def "constructor"() {
    when:
    def subject = new BaseAccessorProxySingleton(AccessorConfiguration.builder().clientId("client1").build(), BaseAccessorImpl.class);

    then:
    subject.getConfiguration() != null
    subject.getAccessorClass() == BaseAccessorImpl.class
  }

  def "build returns the same instance"() {
    given:
    def subject = new BaseAccessorProxySingleton(AccessorConfiguration.builder().clientId("client1").build(), BaseAccessorImpl.class);

    when:
    def instance = subject.build()

    then:
    instance == subject.build()
  }

  def "calls accessors on instance"() {
    given:
    def accessorInstance = org.mockito.Mockito.mock(BaseAccessor)
    def subject = new BaseAccessorProxySingleton(AccessorConfiguration.builder().clientId("client1").build(), BaseAccessorImpl.class, accessorInstance);

    when:
    subject.accounts()
    subject.id()

    then:
    org.mockito.Mockito.verify(accessorInstance).accounts() || true
    org.mockito.Mockito.verify(accessorInstance).id()       || true
  }
}
