package com.mx.path.gateway.configuration

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.BaseAccessor
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxySingleton
import com.mx.testing.BaseAccessorImpl

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
    def accessorInstance = mock(BaseAccessor)
    def subject = new BaseAccessorProxySingleton(AccessorConfiguration.builder().clientId("client1").build(), BaseAccessorImpl.class, accessorInstance);

    when:
    subject.remoteDeposits()
    subject.status()
    subject.accounts()
    subject.transfers()
    subject.payouts()
    subject.payments()
    subject.profiles()
    subject.locations()
    subject.documents()
    subject.managedCards()
    subject.id()
    subject.authorizations()
    subject.creditReports()

    then:
    verify(accessorInstance).remoteDeposits() || true
    verify(accessorInstance).status() || true
    verify(accessorInstance).accounts() || true
    verify(accessorInstance).transfers() || true
    verify(accessorInstance).payouts() || true
    verify(accessorInstance).payments() || true
    verify(accessorInstance).profiles() || true
    verify(accessorInstance).locations() || true
    verify(accessorInstance).documents() || true
    verify(accessorInstance).managedCards() || true
    verify(accessorInstance).id() || true
    verify(accessorInstance).authorizations() || true
    verify(accessorInstance).creditReports() || true
  }
}
